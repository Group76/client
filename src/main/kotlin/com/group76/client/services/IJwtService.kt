package com.group76.client.services

interface IJwtService {
    fun generateToken(id: String) : String?
    fun extractId(token: String): String?
    fun isExpired(token: String): Boolean
}