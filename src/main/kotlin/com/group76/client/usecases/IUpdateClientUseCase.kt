package com.group76.client.usecases

import com.group76.client.entities.request.UpdateClientRequest
import com.group76.client.entities.response.BaseResponse
import com.group76.client.entities.response.GetClientInformationResponse

interface IUpdateClientUseCase {
    fun execute(
        payload: UpdateClientRequest,
        token: String
    ): BaseResponse<GetClientInformationResponse>
}