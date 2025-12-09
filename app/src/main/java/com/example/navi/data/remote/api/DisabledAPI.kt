package com.example.navi.data.remote.api

import com.example.navi.data.remote.dto.DisabledDTO
import com.example.navi.data.remote.dto.LocationDTO
import com.example.navi.data.remote.dto.RefreshTokenDTO
import com.example.navi.data.remote.dto.TokenDTO
import com.example.navi.data.remote.dto.VolunteerLocationDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DisabledAPI {

    @GET("/api/needy/me")
    suspend fun me(): Response<DisabledDTO>

    @POST("/api/needy/update_location")
    suspend fun updateLocation(@Body location: LocationDTO): Response<Void>

    @POST("/api/needy/search_in_radius")
    suspend fun searchVolunteersInRadius(
        @Query("radius_km") radius: Int,
        @Body location: LocationDTO
    ): Response<List<VolunteerLocationDTO>>

    @POST("/api/auth/refresh")
    suspend fun refreshToken(@Body refreshToken: RefreshTokenDTO): Response<TokenDTO>
}