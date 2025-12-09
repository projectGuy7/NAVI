package com.example.navi.domain.repository

import com.example.navi.data.remote.dto.LocationDTO
import com.example.navi.data.remote.dto.RefreshTokenDTO
import com.example.navi.data.remote.dto.TokenDTO
import com.example.navi.domain.navi.Disabled
import com.example.navi.domain.navi.Token
import com.example.navi.domain.navi.VolunteerLocation
import com.example.navi.domain.util.Resource

interface DisabledRepository {

    suspend fun me(): Resource<Disabled>

    suspend fun updateLocation(location: LocationDTO): Resource<Void>

    suspend fun searchInRadius(radius: Int, location: LocationDTO): Resource<List<VolunteerLocation>>

    suspend fun refreshToken(refreshToken: RefreshTokenDTO): Resource<Token>

    suspend fun disposeApi(accessToken: String)

}