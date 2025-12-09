package com.example.navi.data.remote.api

import com.example.navi.data.remote.dto.EmailVerificationDTO
import com.example.navi.data.remote.dto.TokenDTO
import com.example.navi.data.remote.dto.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApi {

    @POST("/api/auth/register")
    suspend fun register(@Body userDTO: UserDTO): Response<Void>

    @POST("/api/auth/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<TokenDTO>

    @POST("/api/auth/verify-code")
    suspend fun verifyCode(
        @Body emailVerification: EmailVerificationDTO
    ): Response<TokenDTO>


}