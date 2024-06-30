package com.group76.client.entities.response

import org.springframework.http.HttpStatus

data class BaseResponse<Any>(
    val data: Any?,
    val error: String?,
    val statusCodes: HttpStatus = HttpStatus.OK
)
