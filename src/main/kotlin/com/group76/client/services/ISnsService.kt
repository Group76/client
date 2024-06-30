package com.group76.client.services

import com.group76.client.entities.request.ClientMessageSns
import software.amazon.awssdk.http.SdkHttpResponse

interface ISnsService {
    fun publishMessage(
        topicArn: String,
        message: ClientMessageSns,
        subject: String
    ): SdkHttpResponse

    fun getTopicArnByName(
        topicName: String
    ): String?
}