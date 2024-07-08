package com.group76.client.utils

class StringHelper {
    companion object {
        fun removeSpecialCharactersAndSpaces(input: String?): String? {
            if(input.isNullOrEmpty()) return input

            return input.filter { it.isLetterOrDigit() }
        }
    }
}