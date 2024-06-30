package com.group76.client.services.impl

import com.group76.client.services.IHashService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class HashServiceImpl: IHashService {
    override fun hash(value: String): String {
        val encoder = BCryptPasswordEncoder()
        return encoder.encode(value)
    }

    override fun checkPassword(plainPassword: String, hashedPassword: String): Boolean {
        val encoder = BCryptPasswordEncoder()
        return encoder.matches(plainPassword, hashedPassword)
    }
}