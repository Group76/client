package com.group76.client.controllers.v1

import com.group76.client.controllers.v1.mapping.UrlMapping
import com.group76.client.entities.request.CreateClientRequest
import com.group76.client.entities.request.GetClientRequest
import com.group76.client.entities.request.UpdateClientRequest
import com.group76.client.entities.response.GetClientInformationResponse
import com.group76.client.usecases.ICreateClientUseCase
import com.group76.client.usecases.IGetClientUseCase
import com.group76.client.usecases.IUpdateClientUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(UrlMapping.Version.V1 + UrlMapping.Resource.USER)
class ClientController(
    private val createClientUseCase: ICreateClientUseCase,
    private val getClientUseCase: IGetClientUseCase,
    private val updateClientUseCase : IUpdateClientUseCase
) {
    @PostMapping(
        name = "CreateClient"
    )
    @Operation(
        method = "CreateClient",
        description = "Create a client",
        responses = [
            ApiResponse(
                description = "OK", responseCode = "200", content = [
                    Content(schema = Schema(implementation = GetClientInformationResponse::class))
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
    fun createClient(
        @Valid @RequestBody request: CreateClientRequest
    ): ResponseEntity<Any> {
        val response = createClientUseCase.execute(request)

        return ResponseEntity(
            response.error ?: response.data,
            response.statusCodes
        )
    }

    @GetMapping(
        name = "GetClient"
    )
    @Operation(
        method = "GetClient",
        description = "Get a client information",
        responses = [
            ApiResponse(
                description = "OK", responseCode = "200", content = [
                    Content(schema = Schema(implementation = GetClientInformationResponse::class))
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
            ),
            ApiResponse(
                description = "Forbidden", responseCode = "403", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Unauthorized", responseCode = "401", content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            )
        ]
    )
    fun getClient(
        @RequestHeader(value = "Authorization") auth: String
    ): ResponseEntity<Any> {
        val response = getClientUseCase.execute(GetClientRequest(auth))

        return ResponseEntity(
            response.error ?: response.data,
            response.statusCodes
        )
    }

    @PutMapping(
        name = "UpdateClient"
    )
    @Operation(
        method = "UpdateClient",
        description = "Update a client",
        responses = [
            ApiResponse(
                description = "OK", responseCode = "200", content = [
                    Content(schema = Schema(implementation = GetClientInformationResponse::class))
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
    fun updateClient(
        @Valid @RequestBody request: UpdateClientRequest,
        @RequestHeader(value = "Authorization") auth: String
    ): ResponseEntity<Any> {
        val response = updateClientUseCase.execute(request, auth)

        return ResponseEntity(
            response.error ?: response.data,
            response.statusCodes
        )
    }
}