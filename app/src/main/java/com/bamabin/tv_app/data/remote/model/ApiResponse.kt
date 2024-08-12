package com.bamabin.tv_app.data.remote.model

data class ApiResponse<T>(
    val status: Boolean,
    val message: String? = null,
    val results: T? = null,
    val result: T? = null
) {
    fun getMainResult(): T?{
        return results ?: result
    }
}
