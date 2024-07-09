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
    override fun putItem(clientEntity: ClientEntity) : PutItemResponse {
        val dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .build()

        val itemValues = mutableMapOf(
            "id" to AttributeValue.builder().s(clientEntity.id.toString()).build(),
            "password" to AttributeValue.builder().s(clientEntity.password).build()
        )

        if (!clientEntity.email.isNullOrEmpty())
            itemValues["email"] = AttributeValue.builder().s(clientEntity.email).build()

        if (!clientEntity.name.isNullOrEmpty())
            itemValues["user_name"] = AttributeValue.builder().s(clientEntity.name).build()

        if (!clientEntity.phone.isNullOrEmpty())
            itemValues["phone"] = AttributeValue.builder().s(clientEntity.phone).build()

        if (!clientEntity.address.isNullOrEmpty())
            itemValues["address"] = AttributeValue.builder().s(clientEntity.address).build()

        if (!clientEntity.document.isNullOrEmpty())
            itemValues["document"] = AttributeValue.builder().s(clientEntity.document).build()

        val putItemRequest = PutItemRequest.builder()
            .tableName(tableName)
            .item(itemValues)
            .build()

        val response = dynamoDbClient.putItem(putItemRequest)
        dynamoDbClient.close()
        return response
    }

    override fun verifyEmail(email: String, id: String?): Boolean {
        return scan("email", email, "id", id).count() > 0
    }

    override fun verifyDocument(document: String, id: String?): Boolean {
        return scan("document", document, "id", id).count() > 0
    }

    override fun getById(id: String): GetItemResponse {
        val key = mapOf("id" to AttributeValue.builder().s(id).build())
        val client = DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .build()

        val updateRequest = GetItemRequest.builder()
            .tableName(tableName)
            .key(key)
            .build()

        val item = client.getItem(updateRequest)
        client.close()
        return item
    }

    override fun getByEmail(email: String): ScanResponse {
        return scan("email", email)
    }

    override fun getByDocument(document: String): ScanResponse {
        return scan("document", document)
    }

    fun scan(
        attributeName: String,
        value: String,
        attributeNameDiff: String? = null,
        valueNotEqual: String? = null
    ): ScanResponse {
        val dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .build()

        val scanRequest = if(attributeNameDiff == null || valueNotEqual == null)
            ScanRequest.builder()
            .tableName(tableName)
            .filterExpression("#attr = :value")
            .expressionAttributeNames(mapOf("#attr" to attributeName))
            .expressionAttributeValues(mapOf(":value" to AttributeValue.builder().s(value).build()))
            .build()
        else ScanRequest.builder()
            .tableName(tableName)
            .filterExpression("#attr = :value AND #notEqualAttr <> :notEqualValue")
            .expressionAttributeNames(mapOf(
                "#attr" to attributeName,
                "#notEqualAttr" to attributeNameDiff
            ))
            .expressionAttributeValues(mapOf(
                ":value" to AttributeValue.builder().s(value).build(),
                ":notEqualValue" to AttributeValue.builder().s(valueNotEqual).build()
            ))
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

    override fun updateItem(clientEntity: ClientEntity): UpdateItemResponse {
        val dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .build()

        var updateExpression = "SET password = :password"

        val itemValues = mutableMapOf(
            ":password" to AttributeValue.builder().s(clientEntity.password).build()
        )

        if (!clientEntity.email.isNullOrEmpty()) {
            itemValues[":email"] = AttributeValue.builder().s(clientEntity.email).build()
            updateExpression = updateExpression.plus(", email = :email")
        }

        if (!clientEntity.name.isNullOrEmpty()) {
            itemValues[":user_name"] = AttributeValue.builder().s(clientEntity.name).build()
            updateExpression = updateExpression.plus(", user_name = :user_name")
        }

        if (!clientEntity.phone.isNullOrEmpty()) {
            itemValues[":phone"] = AttributeValue.builder().s(clientEntity.phone).build()
            updateExpression = updateExpression.plus(", phone = :phone")
        }

        if (!clientEntity.address.isNullOrEmpty()) {
            itemValues[":address"] = AttributeValue.builder().s(clientEntity.address).build()
            updateExpression = updateExpression.plus(", address = :address")
        }

        if (!clientEntity.document.isNullOrEmpty()) {
            itemValues[":document"] = AttributeValue.builder().s(clientEntity.document).build()
            updateExpression = updateExpression.plus(", document = :document")
        }

        val updateItemRequest = UpdateItemRequest.builder()
            .tableName(tableName)
            .key(mapOf("id" to AttributeValue.builder().s(clientEntity.id.toString()).build()))
            .updateExpression(updateExpression)
            .expressionAttributeValues(itemValues)
            .returnValues(ReturnValue.UPDATED_NEW)
            .build()

        val result = dynamoDbClient.updateItem(updateItemRequest)
        dynamoDbClient.close()
        return result
    }
}