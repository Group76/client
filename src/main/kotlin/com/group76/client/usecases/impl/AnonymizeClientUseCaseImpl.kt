package com.group76.client.usecases.impl

import com.group76.client.entities.request.AnonymizeClientRequest
import com.group76.client.entities.response.BaseResponse
import com.group76.client.services.IDynamoDbService
import com.group76.client.usecases.IAnonymizeClientUseCase
import org.springframework.stereotype.Service

@Service
class AnonymizeClientUseCaseImpl(
    private val dynamo: IDynamoDbService
): IAnonymizeClientUseCase {
    override fun execute(payload: AnonymizeClientRequest): BaseResponse<Any> {
        dynamo.anonymizeClient(payload.id)

        return BaseResponse(
            data = null,
            error = null
        )
    }
}