package com.group76.client.usecases

import com.group76.client.entities.request.GetTokenByEmailRequest
import com.group76.client.entities.response.BaseResponse
import com.group76.client.entities.response.GetTokenResponse

interface IGetTokenByEmailUseCase {
    fun execute(
     payload: GetTokenByEmailRequest
    ): BaseResponse<GetTokenResponse>
}