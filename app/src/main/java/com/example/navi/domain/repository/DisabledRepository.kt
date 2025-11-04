package com.example.navi.domain.repository

import com.example.navi.data.remote.dto.LocationDTO
import com.example.navi.domain.navi.Disabled
import com.example.navi.domain.util.Resource

interface DisabledRepository {

    suspend fun me(): Resource<Disabled>

    suspend fun updateLocation(location: LocationDTO): Resource<Void>

}