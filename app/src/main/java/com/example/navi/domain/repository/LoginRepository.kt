package com.example.navi.domain.repository

import com.example.navi.data.remote.dto.UserDTO
import com.example.navi.domain.navi.Token
import com.example.navi.domain.util.Resource

interface LoginRepository {
    suspend fun register(user: UserDTO): Resource<Void>

    suspend fun verifyCode(email: String, code: String): Resource<Token>

}