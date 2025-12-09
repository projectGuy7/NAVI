package com.example.navi.data.repository

import android.util.Log
import com.example.navi.cryptoManager.TokenCryptoManager
import com.example.navi.data.mappers.toDisabled
import com.example.navi.data.mappers.toToken
import com.example.navi.data.mappers.toVolunteerLocation
import com.example.navi.data.remote.api.DisabledAPI
import com.example.navi.data.remote.api.createDisabledApi
import com.example.navi.data.remote.dto.LocationDTO
import com.example.navi.data.remote.dto.RefreshTokenDTO
import com.example.navi.data.remote.interceptor.AuthorizationInterceptor
import com.example.navi.data.remote.util.formErrorMessage
import com.example.navi.di.qualifiers.FilesDir
import com.example.navi.domain.navi.Disabled
import com.example.navi.domain.navi.Token
import com.example.navi.domain.navi.VolunteerLocation
import com.example.navi.domain.repository.DisabledRepository
import com.example.navi.domain.util.Resource
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

class DisabledRepositoryImpl @Inject constructor(@FilesDir filesDir: File): DisabledRepository {
    var disabledApi: DisabledAPI

    init {
        val cryptoManager = TokenCryptoManager()
        disabledApi = createDisabledApi(AuthorizationInterceptor(
            cryptoManager.decrypt(
                FileInputStream(File(filesDir, "token.txt"))
                ).accessToken
            )
        )
    }

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

    override suspend fun searchInRadius(radius: Int, location: LocationDTO): Resource<List<VolunteerLocation>> {
        val response = disabledApi.searchVolunteersInRadius(radius, location)

        return if(response.isSuccessful) {
            Resource.Success(
                data = response.body()?.map({ it.toVolunteerLocation() })
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
        return if(response.isSuccessful) {
            Resource.Success(
                data = response.body()?.toToken()
            )
        } else {
            Resource.Error(
                message = formErrorMessage(response),
                responseCode = response.code()
            )
        }
    }

    override suspend fun disposeApi(accessToken: String) {
        disabledApi = createDisabledApi(AuthorizationInterceptor(accessToken))
    }

}