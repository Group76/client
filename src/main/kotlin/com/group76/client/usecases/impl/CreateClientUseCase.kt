package com.group76.client.usecases.impl

import com.group76.client.configuration.SystemProperties
import com.group76.client.entities.ClientEntity
import com.group76.client.entities.enum.ClientOperation
import com.group76.client.entities.request.ClientMessageSns
import com.group76.client.entities.request.CreateClientRequest
import com.group76.client.entities.response.BaseResponse
import com.group76.client.entities.response.CreateClientResponse
import com.group76.client.services.IDynamoDbService
import com.group76.client.services.ISnsService
import com.group76.client.usecases.ICreateClientUseCase
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreateClientUseCase(
    private val dynamo: IDynamoDbService,
    private val snsService: ISnsService,
    private val systemProperties: SystemProperties
) : ICreateClientUseCase {
    override fun execute(payload: CreateClientRequest): BaseResponse<CreateClientResponse> {
        val client = ClientEntity(
            name = payload.name,
            id = UUID.randomUUID(),
            email = payload.email,
            phone = payload.phone,
            document = payload.document,
            password = payload.password,
            address = payload.address
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
                password = client.password,
                address = client.password,
                id = client.id,
                document = client.document
            ),
            error = null
        )
    }
}