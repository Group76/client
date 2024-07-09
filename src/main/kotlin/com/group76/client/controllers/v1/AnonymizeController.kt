package com.group76.client.controllers.v1

import com.group76.client.controllers.v1.mapping.UrlMapping
import com.group76.client.entities.request.AnonymizeClientRequest
import com.group76.client.entities.response.CreateClientResponse
import com.group76.client.usecases.IAnonymizeClientUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(UrlMapping.Version.V1 + UrlMapping.Resource.ANONYMIZE)
class AnonymizeController(
    private val anonymizeClientUseCase: IAnonymizeClientUseCase
) {
    val logger: org.slf4j.Logger = LoggerFactory.getLogger(LoggerFactory::class.java)

    @DeleteMapping(
        name = "AnonymizeClient"
    )
    @Operation(
        method = "AnonymizeClient",
        description = "Anonymize a client",
        responses = [
            ApiResponse(
                description = "OK", responseCode = "200", content = [
                    Content(schema = Schema(implementation = CreateClientResponse::class))
                ]
            ),
            ApiResponse(
                description = "Bad Request", responseCode = "400", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Internal Error", responseCode = "500", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            )
        ]
    )
    fun anonymizeClient(
        @RequestHeader(value = "Authorization") auth: String
    ): ResponseEntity<Any> {
        logger.info("auth: $auth")
        val response = anonymizeClientUseCase.execute(AnonymizeClientRequest(auth))

        return ResponseEntity(
            response.error ?: response.data,
            response.statusCodes
        )
    }
}