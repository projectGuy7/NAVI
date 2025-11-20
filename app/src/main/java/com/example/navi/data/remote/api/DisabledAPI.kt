package com.example.navi.data.remote.api

import com.example.navi.data.remote.dto.DisabledDTO
import com.example.navi.data.remote.dto.LocationDTO
import com.example.navi.data.remote.dto.RefreshTokenDTO
import com.example.navi.data.remote.dto.TokenDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DisabledAPI {

    @GET("/api/needy/me")
    suspend fun me(): Response<DisabledDTO>

    @POST("/api/needy/location")
    suspend fun updateLocation(@Body location: LocationDTO): Response<Void>

    @POST("/api/auth/refresh")
    suspend fun refreshToken(@Body refreshToken: RefreshTokenDTO): Response<TokenDTO>
}