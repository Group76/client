package com.group76.client.usecases

import com.group76.client.entities.request.GetClientRequest
import com.group76.client.entities.response.BaseResponse
import com.group76.client.entities.response.GetClientInformationResponse

interface IGetClientUseCase {
    fun execute(
        payload: GetClientRequest
    ): BaseResponse<GetClientInformationResponse>
}