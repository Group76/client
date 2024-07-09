package com.group76.client.services.impl

import com.group76.client.entities.ClientEntity
import com.group76.client.services.IDynamoDbService
import org.springframework.stereotype.Component
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*

@Component
class DynamoDbServiceImpl : IDynamoDbService {
    private val tableName = "Client"
    override fun putItem(clientEntity: ClientEntity) {
        val dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .build()

        val itemValues = mutableMapOf(
            "id" to AttributeValue.builder().s(clientEntity.id.toString()).build(),
            "password" to AttributeValue.builder().s(clientEntity.password).build()
        )

        if(!clientEntity.email.isNullOrEmpty())
            itemValues["email"] = AttributeValue.builder().s(clientEntity.email).build()

        if(!clientEntity.name.isNullOrEmpty())
            itemValues["user_name"] = AttributeValue.builder().s(clientEntity.name).build()

        if(!clientEntity.phone.isNullOrEmpty())
            itemValues["phone"] = AttributeValue.builder().s(clientEntity.phone).build()

        if(!clientEntity.address.isNullOrEmpty())
            itemValues["address"] = AttributeValue.builder().s(clientEntity.address).build()

        if(!clientEntity.document.isNullOrEmpty())
            itemValues["document"] = AttributeValue.builder().s(clientEntity.document).build()

        val putItemRequest = PutItemRequest.builder()
            .tableName(tableName)
            .item(itemValues)
            .build()

        dynamoDbClient.putItem(putItemRequest)
        dynamoDbClient.close()
    }

    override fun verifyEmail(email: String): Boolean {
        return scan("email", email).count() > 0
    }

    override fun verifyDocument(document: String): Boolean {
        return scan("document", document).count() > 0
    }

    override fun getByEmail(email: String): ScanResponse {
        return scan("email", email)
    }

    override fun getByDocument(document: String): ScanResponse {
        return scan("document", document)
    }

    fun scan(attributeName: String, value: String): ScanResponse {
        val dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .build()

        val scanRequest = ScanRequest.builder()
            .tableName(tableName)
            .filterExpression("#attr = :value")
            .expressionAttributeNames(mapOf("#attr" to attributeName))
            .expressionAttributeValues(mapOf(":value" to AttributeValue.builder().s(value).build()))
            .build()

        val result = dynamoDbClient.scan(scanRequest)
        dynamoDbClient.close()
        return result
    }

    override fun anonymizeClient(id: String) {
        val attributesToRemove = listOf(
            "email",
            "user_name",
            "phone",
            "address",
            "document"
        )

        val updateExpression = attributesToRemove.joinToString(separator = ", ", prefix = "REMOVE ") { it }
        val key = mapOf("id" to AttributeValue.builder().s(id).build())
        val client = DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .build()

        val updateRequest = UpdateItemRequest.builder()
            .tableName(tableName)
            .key(key)
            .updateExpression(updateExpression)
            .build()

        client.updateItem(updateRequest)
        client.close()
    }
}