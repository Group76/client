package com.group76.client.services

import com.group76.client.entities.ClientEntity
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse
import software.amazon.awssdk.services.dynamodb.model.ScanResponse
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse

interface IDynamoDbService {
    fun putItem(clientEntity: ClientEntity) : PutItemResponse
    fun verifyEmail(email: String, id: String? = null): Boolean
    fun verifyDocument(document: String, id: String? = null): Boolean
    fun getById(id: String): GetItemResponse
    fun getByEmail(email: String): ScanResponse
    fun getByDocument(document: String): ScanResponse
    fun anonymizeClient(id: String)
    fun updateItem(clientEntity: ClientEntity) : UpdateItemResponse
}