package com.group76.client.services.impl

import com.group76.client.entities.ClientEntity
import com.group76.client.services.IJwtService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtServiceImpl : IJwtService {
    private val secretKey = "D9texL9_fknC5cb0h-ik2INJyzdona14ZlHoLuOA8nE="
    private val key: SecretKey = Keys.hmacShaKeyFor(secretKey.toByteArray())
    override fun generateToken(id: String): String? {
        val now = Date()
        val validity = Date(now.time + 3600000) // 1 hour validity

        return Jwts.builder()
            .expiration(validity)
            .issuedAt(now)
            .claim("id", id)
            .signWith(key)
            .compact()
    }
}