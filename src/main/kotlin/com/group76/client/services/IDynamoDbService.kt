package com.group76.client.services

import com.group76.client.entities.ClientEntity

interface IDynamoDbService {
    fun putItem(clientEntity: ClientEntity)
    fun verifyEmail(email: String): Boolean
    fun verifyDocument(document: String): Boolean
}