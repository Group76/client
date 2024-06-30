package com.group76.client.services.impl

import com.group76.client.entities.ClientEntity
import com.group76.client.services.IDynamoDbService
import org.springframework.stereotype.Component
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest

@Component
class DynamoDbServiceImpl : IDynamoDbService {
    override fun putItem(clientEntity: ClientEntity) {
        val dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .build()

        val itemValues = mapOf(
            "id" to AttributeValue.builder().s(clientEntity.id.toString()).build(),
            "email" to AttributeValue.builder().s(clientEntity.email).build(),
            "password" to AttributeValue.builder().s(clientEntity.password).build()
        )

        val putItemRequest = PutItemRequest.builder()
            .tableName("Client")
            .item(itemValues)
            .build()

        dynamoDbClient.putItem(putItemRequest)
    }
}