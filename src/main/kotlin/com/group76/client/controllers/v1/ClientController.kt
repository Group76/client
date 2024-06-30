package com.group76.client.controllers.v1

import com.group76.client.controllers.v1.mapping.UrlMapping
import com.group76.client.entities.request.CreateClientRequest
import com.group76.client.entities.response.CreateClientResponse
import com.group76.client.usecases.ICreateClientUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(UrlMapping.Version.V1 + UrlMapping.Resource.CLIENT)
class ClientController(
    private val createClientUseCase: ICreateClientUseCase
) {
    val logger: Logger = LoggerFactory.getLogger(LoggerFactory::class.java)

    @PostMapping(
        name = "CreateClient"
    )
    @Operation(
        method = "CreateClient",
        description = "Create a client",
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
    fun createProduct(
        @Valid @RequestBody request: CreateClientRequest
    ): ResponseEntity<Any> {
        val response = createClientUseCase.execute(request)

        return ResponseEntity(
            response.error ?: response.data,
            response.statusCodes
        )
    }
}