package com.group76.client.services

import com.group76.client.entities.ClientEntity
import software.amazon.awssdk.services.dynamodb.model.ScanResponse

interface IDynamoDbService {
    fun putItem(clientEntity: ClientEntity)
    fun verifyEmail(email: String): Boolean
    fun verifyDocument(document: String): Boolean
    fun getByEmail(email: String): ScanResponse
    fun getByDocument(document: String): ScanResponse
}