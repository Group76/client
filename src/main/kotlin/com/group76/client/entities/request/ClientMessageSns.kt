package com.group76.client.entities.request

import com.group76.client.entities.enum.ClientOperation

data class ClientMessageSns(
    val id: String,
    val operation: ClientOperation
)
