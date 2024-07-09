package com.group76.client.utils

import java.util.UUID

object Helper {

    fun removeSpecialCharactersAndSpaces(input: String?): String? {
        if (input.isNullOrEmpty()) return input
        return input.filter { it.isLetterOrDigit() }
    }

    fun getGuestId(): UUID {
        return UUID.fromString("83f3ea51-dacc-430c-ac34-8d64907d9a7c")
    }
}