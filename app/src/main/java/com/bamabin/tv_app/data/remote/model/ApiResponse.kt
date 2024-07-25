package com.bamabin.tv_app.data.remote.model

data class ApiResponse<T>(
    val status: Boolean,
    val result: T? = null
)
