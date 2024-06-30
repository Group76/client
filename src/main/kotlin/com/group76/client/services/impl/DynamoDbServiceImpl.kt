package com.group76.client.services.impl

import com.group76.client.entities.ClientEntity
import com.group76.client.services.IDynamoDbService
import org.springframework.stereotype.Component
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.ScanRequest
import software.amazon.awssdk.services.dynamodb.model.ScanResponse

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
            itemValues["name"] = AttributeValue.builder().s(clientEntity.name).build()

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
    }

    override fun verifyEmail(email: String): Boolean {
        return verifyExists("email", email)
    }

    override fun verifyDocument(document: String): Boolean {
        return verifyExists("document", document)
    }

    fun verifyExists(attributeName: String, value: String): Boolean {
        val dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .build()

        val scanRequest = ScanRequest.builder()
            .tableName(tableName)
            .filterExpression("#attr = :value")
            .expressionAttributeNames(mapOf("#attr" to attributeName))
            .expressionAttributeValues(mapOf(":value" to AttributeValue.builder().s(value).build()))
            .build()

        val scanResponse: ScanResponse = dynamoDbClient.scan(scanRequest)
        return scanResponse.count() > 0
    }
}