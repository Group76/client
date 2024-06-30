package com.group76.client.entities.request

data class CreateClientRequest (
    val password: String,
    val document: String?,
    val email: String?,
    val name: String?,
    val address: String?,
    val phone: String?
){
    fun getError(): String? {
        if(password.length < 8)
        return "Password not long enough, should be at least 8 characters"

        if(document.isNullOrEmpty()
            && email.isNullOrEmpty())
            return "Client has to be at least one form of identification: Document or E-mail"

        return null
    }
}