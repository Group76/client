package com.group76.client.usecases.impl

import com.group76.client.configuration.SystemProperties
import com.group76.client.entities.ClientEntity
import com.group76.client.entities.enum.ClientOperation
import com.group76.client.entities.request.ClientMessageSns
import com.group76.client.entities.request.CreateClientRequest
import com.group76.client.entities.request.UpdateClientRequest
import com.group76.client.entities.response.BaseResponse
import com.group76.client.entities.response.GetClientInformationResponse
import com.group76.client.services.IDynamoDbService
import com.group76.client.services.IHashService
import com.group76.client.services.IJwtService
import com.group76.client.services.ISnsService
import com.group76.client.usecases.ICreateClientUseCase
import com.group76.client.usecases.IUpdateClientUseCase
import com.group76.client.utils.Helper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class UpdateClientUseCaseImpl(
    private val dynamo: IDynamoDbService,
    private val snsService: ISnsService,
    private val systemProperties: SystemProperties,
    private val hashService: IHashService,
    private val jwtService: IJwtService
) : IUpdateClientUseCase {
    override fun execute(payload: UpdateClientRequest, token: String): BaseResponse<GetClientInformationResponse> {
        val error = payload.getError()

        if(error != null)
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError(error),
                statusCodes = HttpStatus.BAD_REQUEST
            )

        val id = jwtService.extractId(token)

        if(id == null
            || jwtService.isExpired(token)) {
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

        val client = ClientEntity(
            name = payload.name,
            id = UUID.fromString(id),
            email = payload.email,
            phone = payload.phone,
            document = Helper.removeSpecialCharactersAndSpaces(payload.document),
            password = hashService.hash(payload.password),
            address = payload.address
        )

        if(!client.email.isNullOrEmpty()
            && dynamo.verifyEmail(client.email, id))
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError("E-mail already exists."),
                statusCodes = HttpStatus.BAD_REQUEST
            )

        if(!client.document.isNullOrEmpty()
            && dynamo.verifyDocument(client.document, id))
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError("Document already exists."),
                statusCodes = HttpStatus.BAD_REQUEST
            )

        val response = dynamo.updateItem(client)

        if(!response.sdkHttpResponse().isSuccessful){
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError("Error while updating client."),
                statusCodes = HttpStatus.INTERNAL_SERVER_ERROR
            )
        }

        snsService.publishMessage(
            snsService.getTopicArnByName(systemProperties.sns.client)!!,
            ClientMessageSns(client.id.toString(), ClientOperation.UPDATED),
            "Client updated"
        )

        return BaseResponse(
            data = GetClientInformationResponse(
                email = client.email,
                phone = client.phone,
                name = client.name,
                address = client.address,
                id = client.id,
                document = client.document,
                token = jwtService.generateToken(client.id.toString())
            ),
            error = null
        )
    }
}