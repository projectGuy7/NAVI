package com.example.navi.data.repository

import com.example.navi.data.mappers.toToken
import com.example.navi.data.remote.api.LoginApi
import com.example.navi.data.remote.dto.EmailVerificationDTO
import com.example.navi.data.remote.dto.UserDTO
import com.example.navi.data.remote.util.formErrorMessage
import com.example.navi.domain.navi.Token
import com.example.navi.domain.repository.LoginRepository
import com.example.navi.domain.util.Resource
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val api: LoginApi): LoginRepository {
    override suspend fun register(user: UserDTO): Resource<Void> {
        val response = api.register(user)

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

    override suspend fun login(username: String, password: String): Resource<Token> {
        val response = api.login(username, password)

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

    override suspend fun verifyCode(
        emailVerification: EmailVerificationDTO
    ): Resource<Token> {
        val response = api.verifyCode(emailVerification)

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
}