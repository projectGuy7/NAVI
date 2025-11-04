package com.example.navi.data.remote.api

import com.example.navi.data.remote.dto.TokenDTO
import com.example.navi.data.remote.dto.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApi {

    @POST("/api/auth/register")
    suspend fun register(@Body userDTO: UserDTO): Response<Void>

    @POST("/api/auth/verify-code")
    suspend fun verifyCode(@Query("email") email: String, @Query("code") code: String): Response<TokenDTO>


}