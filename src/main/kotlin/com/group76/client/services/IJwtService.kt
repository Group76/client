package com.group76.client.services

interface IJwtService {
    fun generateToken(id: String) : String?
}