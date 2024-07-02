package com.group76.client.usecases.impl

import com.group76.client.entities.request.GetTokenByDocumentRequest
import com.group76.client.entities.response.BaseResponse
import com.group76.client.entities.response.GetTokenResponse
import com.group76.client.services.IDynamoDbService
import com.group76.client.services.IHashService
import com.group76.client.services.IJwtService
import com.group76.client.usecases.IGetTokenByDocumentUseCase
import com.group76.client.utils.StringHelper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class GetTokenByDocumentUseCase(
    private val dynamo: IDynamoDbService,
    private val hashService: IHashService,
    private val jtwService: IJwtService
) : IGetTokenByDocumentUseCase {
    override fun execute(payload: GetTokenByDocumentRequest): BaseResponse<GetTokenResponse> {
        val scanResponse = dynamo
            .getByDocument(StringHelper.removeSpecialCharactersAndSpaces(payload.document)!!)

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