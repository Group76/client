package com.group76.client.usecases

import com.group76.client.entities.request.AnonymizeClientRequest
import com.group76.client.entities.response.BaseResponse

interface IAnonymizeClientUseCase {
    fun execute(payload: AnonymizeClientRequest) : BaseResponse<Any>
}