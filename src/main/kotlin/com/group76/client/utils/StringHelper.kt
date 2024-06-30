package com.group76.client.utils

class StringHelper {
    companion object {
        fun removeSpecialCharactersAndSpaces(input: String?): String? {
            if(input.isNullOrEmpty()) return input

            val regex = Regex("[^a-zA-Z0-9.-]+")
            return input.replace(regex, "")
        }
    }
}