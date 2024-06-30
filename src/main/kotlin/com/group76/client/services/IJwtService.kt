package com.group76.client.services

import com.group76.client.entities.ClientEntity

interface IJwtService {
    fun generateToken(clientEntity: ClientEntity) : String?
}