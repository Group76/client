package com.group76.client.entities.request

data class GetTokenByDocumentRequest(
    val document: String,
    val password: String
)
