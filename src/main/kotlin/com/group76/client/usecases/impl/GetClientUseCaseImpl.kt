package com.group76.client.usecases.impl

import com.group76.client.entities.ClientEntity
import com.group76.client.entities.request.GetClientRequest
import com.group76.client.entities.response.BaseResponse
import com.group76.client.entities.response.GetClientInformationResponse
import com.group76.client.services.IDynamoDbService
import com.group76.client.services.IJwtService
import com.group76.client.usecases.IGetClientUseCase
import com.group76.client.utils.Helper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class GetClientUseCaseImpl(
    private val dynamo: IDynamoDbService,
    private val jwtService: IJwtService
) : IGetClientUseCase {
    override fun execute(payload: GetClientRequest): BaseResponse<GetClientInformationResponse> {
        val id = jwtService.extractId(payload.token)

        if(id == null
            || jwtService.isExpired(payload.token)) {
            return BaseResponse(
                data = null,
                error = BaseResponse.BaseResponseError("Unauthorized"),
                HttpStatus.UNAUTHORIZED
            )
        }

        if(id == Helper.getGuestId().toString()) {
            return BaseResponse(
                data = null,
                error = BaseResponse.BaseResponseError("Forbidden"),
                HttpStatus.FORBIDDEN
            )
        }

        val result = dynamo.getById(id)

        if(!result.sdkHttpResponse().isSuccessful){
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError("Error while updating client."),
                statusCodes = HttpStatus.INTERNAL_SERVER_ERROR
            )
        }

        if(!result.hasItem()){
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError("Client not found."),
                statusCodes = HttpStatus.BAD_REQUEST
            )
        }

        val item = result.item()

        val client = ClientEntity(
            name = item["user_name"]?.s(),
            id = UUID.fromString(item["id"]?.s()),
            email = item["email"]?.s(),
            phone = item["phone"]?.s(),
            document = item["document"]?.s(),
            password = null,
            address = item["address"]?.s()
        )

        return BaseResponse(
            data = GetClientInformationResponse(
                email = client.email,
                phone = client.phone,
                name = client.name,
                address = client.address,
                id = client.id,
                document = client.document,
                token = null
            ),
            error = null
        )
    }
}