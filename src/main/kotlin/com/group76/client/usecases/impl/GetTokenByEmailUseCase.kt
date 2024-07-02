package com.group76.client.usecases.impl

import com.group76.client.entities.request.GetTokenByEmailRequest
import com.group76.client.entities.response.BaseResponse
import com.group76.client.entities.response.GetTokenResponse
import com.group76.client.services.IDynamoDbService
import com.group76.client.services.IHashService
import com.group76.client.services.IJwtService
import com.group76.client.usecases.IGetTokenByEmailUseCase
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class GetTokenByEmailUseCase(
    private val dynamo: IDynamoDbService,
    private val hashService: IHashService,
    private val jtwService: IJwtService
) : IGetTokenByEmailUseCase {
    override fun execute(payload: GetTokenByEmailRequest): BaseResponse<GetTokenResponse> {
        val scanResponse = dynamo
            .getByEmail(payload.email)

        if (!scanResponse.hasItems())
            return BaseResponse(
                data = null,
                error = BaseResponse.BaseResponseError("Client not found."),
                statusCodes = HttpStatus.BAD_REQUEST
            )

        val item = scanResponse.items().first()
        val password = item["password"]?.s()
        val id = item["id"]?.s()

        if (password.isNullOrEmpty()
            || id.isNullOrEmpty()
        ) {
            return BaseResponse(
                data = null,
                error = BaseResponse.BaseResponseError("Client not found."),
                statusCodes = HttpStatus.BAD_REQUEST
            )
        }

        if (hashService.checkPassword(payload.password, password)) {
            return BaseResponse(
                data = null,
                error = BaseResponse.BaseResponseError("Client not found."),
                statusCodes = HttpStatus.BAD_REQUEST
            )
        }

        return BaseResponse(
            data = GetTokenResponse(jtwService.generateToken(id)!!),
            error = null
        )
    }
}