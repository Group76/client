package com.group76.client.services

interface IHashService {
    fun hash(value: String): String
    fun checkPassword(plainPassword: String, hashedPassword: String): Boolean
}
