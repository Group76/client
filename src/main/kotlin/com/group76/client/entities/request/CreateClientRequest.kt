package com.group76.client.entities.request

data class CreateClientRequest (
    val password: String?,
    val document: String?,
    val email: String?,
    val name: String?,
    val address: String?,
    val phone: String?
)