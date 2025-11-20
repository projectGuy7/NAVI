package com.example.navi.data.repository

import com.example.navi.data.mappers.toDisabled
import com.example.navi.data.mappers.toToken
import com.example.navi.data.remote.api.DisabledAPI
import com.example.navi.data.remote.api.createDisabledApi
import com.example.navi.data.remote.dto.LocationDTO
import com.example.navi.data.remote.dto.RefreshTokenDTO
import com.example.navi.data.remote.interceptor.AuthorizationInterceptor
import com.example.navi.data.remote.util.formErrorMessage
import com.example.navi.domain.navi.Disabled
import com.example.navi.domain.navi.Token
import com.example.navi.domain.repository.DisabledRepository
import com.example.navi.domain.util.Resource

class DisabledRepositoryImpl(private var disabledApi: DisabledAPI): DisabledRepository {

    override suspend fun me(): Resource<Disabled> {
        val response = disabledApi.me()

        return if(response.isSuccessful) {
            Resource.Success(
                data = response.body()?.toDisabled()
            )
        } else {
            Resource.Error(
                message = formErrorMessage(response),
                responseCode = response.code()
            )
        }
    }

    override suspend fun updateLocation(location: LocationDTO): Resource<Void> {
        val response = disabledApi.updateLocation(location)

        return if(response.isSuccessful) {
            Resource.Success(
                data = null
            )
        } else {
            Resource.Error(
                message = formErrorMessage(response),
                responseCode = response.code()
            )
        }
    }

    override suspend fun refreshToken(refreshToken: RefreshTokenDTO): Resource<Token> {
        val response = disabledApi.refreshToken(refreshToken)

        if(response.isSuccessful) {
            return Resource.Success<Token>(
                data = response.body()?.toToken()
            )
        } else {
            return Resource.Error(
                message = formErrorMessage(response),
                responseCode = response.code()
            )
        }
    }

    override suspend fun disposeApi(accessToken: String) {
        disabledApi = createDisabledApi(AuthorizationInterceptor(accessToken))
    }

}