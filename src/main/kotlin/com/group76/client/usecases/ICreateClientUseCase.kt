package com.group76.client.usecases

import com.group76.client.entities.request.CreateClientRequest
import com.group76.client.entities.response.BaseResponse
import com.group76.client.entities.response.CreateClientResponse

interface ICreateClientUseCase {
    fun execute(
        payload: CreateClientRequest
    ): BaseResponse<CreateClientResponse>
}