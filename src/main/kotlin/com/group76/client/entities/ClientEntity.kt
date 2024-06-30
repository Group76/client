package com.group76.client.entities

import java.util.UUID

data class ClientEntity(
    val id: UUID,
    val password: String?,
    val document: String?,
    val email: String?,
    val name: String?,
    val address: String?,
    val phone: String?
)
