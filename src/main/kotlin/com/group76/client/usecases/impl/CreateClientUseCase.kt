package com.group76.client.usecases.impl

import com.group76.client.configuration.SystemProperties
import com.group76.client.entities.ClientEntity
import com.group76.client.entities.enum.ClientOperation
import com.group76.client.entities.request.ClientMessageSns
import com.group76.client.entities.request.CreateClientRequest
import com.group76.client.entities.response.BaseResponse
import com.group76.client.entities.response.CreateClientResponse
import com.group76.client.services.IDynamoDbService
import com.group76.client.services.IHashService
import com.group76.client.services.IJwtService
import com.group76.client.services.ISnsService
import com.group76.client.usecases.ICreateClientUseCase
import com.group76.client.utils.StringHelper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreateClientUseCase(
    private val dynamo: IDynamoDbService,
    private val snsService: ISnsService,
    private val systemProperties: SystemProperties,
    private val hashService: IHashService,
    private val jtwService: IJwtService
) : ICreateClientUseCase {
    override fun execute(payload: CreateClientRequest): BaseResponse<CreateClientResponse> {
        val error = payload.getError()

        if(error != null)
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError(error),
                statusCodes = HttpStatus.BAD_REQUEST
            )

        val client = ClientEntity(
            name = payload.name,
            id = UUID.randomUUID(),
            email = payload.email,
            phone = payload.phone,
            document = StringHelper.removeSpecialCharactersAndSpaces(payload.document),
            password = hashService.hash(payload.password),
            address = payload.address
        )

        if(!client.email.isNullOrEmpty()
            && dynamo.verifyEmail(client.email))
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError("E-mail already exists."),
                statusCodes = HttpStatus.BAD_REQUEST
            )

        if(!client.document.isNullOrEmpty()
            && dynamo.verifyDocument(client.document))
            return BaseResponse(data = null,
                error = BaseResponse.BaseResponseError("Document already exists."),
                statusCodes = HttpStatus.BAD_REQUEST
            )

        dynamo.putItem(client)

        snsService.publishMessage(
            snsService.getTopicArnByName(systemProperties.sns.client)!!,
            ClientMessageSns(client.id.toString(), ClientOperation.CREATED),
            "Client created"
        )

        return BaseResponse(
            data = CreateClientResponse(
                email = client.email,
                phone = client.phone,
                name = client.name,
                address = client.address,
                id = client.id,
                document = client.document,
                token = jtwService.generateToken(client.id.toString())
            ),
            error = null
        )
    }
}