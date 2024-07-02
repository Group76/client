package com.group76.client.usecases

import com.group76.client.entities.request.GetTokenByDocumentRequest
import com.group76.client.entities.response.BaseResponse
import com.group76.client.entities.response.GetTokenResponse

interface IGetTokenByDocumentUseCase {
    fun execute(
     payload: GetTokenByDocumentRequest
    ): BaseResponse<GetTokenResponse>
}