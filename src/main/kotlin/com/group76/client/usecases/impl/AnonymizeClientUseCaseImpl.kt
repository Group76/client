package com.group76.client.usecases.impl

import com.group76.client.entities.request.AnonymizeClientRequest
import com.group76.client.entities.response.BaseResponse
import com.group76.client.services.IDynamoDbService
import com.group76.client.services.IJwtService
import com.group76.client.usecases.IAnonymizeClientUseCase
import com.group76.client.utils.Helper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class AnonymizeClientUseCaseImpl(
    private val dynamo: IDynamoDbService,
    private val jwtService: IJwtService
): IAnonymizeClientUseCase {
    override fun execute(payload: AnonymizeClientRequest): BaseResponse<Any> {
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

        dynamo.anonymizeClient(id)

        return BaseResponse(
            data = null,
            error = null
        )
    }
}