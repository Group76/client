package com.group76.client.usecases

import com.group76.client.entities.response.BaseResponse
import com.group76.client.entities.response.GetTokenResponse

interface IGetGuestTokenUseCase {
    fun execute(): BaseResponse<GetTokenResponse>
}