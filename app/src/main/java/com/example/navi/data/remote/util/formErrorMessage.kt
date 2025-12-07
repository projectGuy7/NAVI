package com.example.navi.data.remote.util

import retrofit2.Response

fun formErrorMessage(response: Response<*>): String {
    if(response.isSuccessful) throw Exception("Successful response passed to formErrorMessage()")

    return "Error code : ${response.code()} Message : ${response.errorBody()?.string()}"
}