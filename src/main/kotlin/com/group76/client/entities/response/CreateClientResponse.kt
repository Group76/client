package com.group76.client.entities.response

import java.util.UUID

data class CreateClientResponse(
    val id: UUID,
    val document: String?,
    val email: String?,
    val name: String?,
    val address: String?,
    val phone: String?,
    val token: String?
)
