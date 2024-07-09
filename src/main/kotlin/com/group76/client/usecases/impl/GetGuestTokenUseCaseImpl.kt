package com.group76.client.usecases.impl

import com.group76.client.entities.response.BaseResponse
import com.group76.client.entities.response.GetTokenResponse
import com.group76.client.services.IJwtService
import com.group76.client.usecases.IGetGuestTokenUseCase
import com.group76.client.utils.Helper
import org.springframework.stereotype.Service

@Service
class GetGuestTokenUseCaseImpl(
    private val jtwService: IJwtService
) : IGetGuestTokenUseCase {
    override fun execute(): BaseResponse<GetTokenResponse> {
        val id = Helper.getGuestId().toString()

        return BaseResponse(
            data = GetTokenResponse(
                token = jtwService.generateToken(id)!!,
                id = null
            ),
            error = null
        )
    }
}