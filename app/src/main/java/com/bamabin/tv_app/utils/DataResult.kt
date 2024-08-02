package com.bamabin.tv_app.utils

sealed class DataResult<T> (val data: T? = null, val message: String = "") {
    class DataSuccess<T> (data: T): DataResult<T>(data)
    class DataError<T> (message: String): DataResult<T>(message = message)
    class DataLoading<T>: DataResult<T>()
}