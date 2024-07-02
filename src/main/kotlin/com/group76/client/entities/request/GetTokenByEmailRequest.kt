package com.group76.client.entities.request

data class GetTokenByEmailRequest(
    val email: String,
    val password: String
)
