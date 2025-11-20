package com.example.navi.domain.util

sealed class Resource<T>(val data: T? = null, val message: String? = null, val responseCode: Int? = null) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String?, responseCode: Int?): Resource<T>(message = message, responseCode = responseCode)
}