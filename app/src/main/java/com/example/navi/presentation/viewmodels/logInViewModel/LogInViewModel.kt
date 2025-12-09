package com.example.navi.presentation.viewmodels.logInViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navi.cryptoManager.TokenCryptoManager
import com.example.navi.data.remote.dto.TokenDTO
import com.example.navi.di.qualifiers.FilesDir
import com.example.navi.domain.navi.Token
import com.example.navi.domain.repository.LoginRepository
import com.example.navi.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(val repository: LoginRepository, @param:FilesDir val filesDir: File): ViewModel() {

    var state by mutableStateOf(LogInState(
        email = "bolathanovazamat047@gmail.com",
        password = "Azamat1234@"
    ))
        private set

    fun onEvent(action: LogInAction) {
        when(action) {
            LogInAction.LogIn -> {
                viewModelScope.launch {
                    when(val result = repository.login(state.email, state.password)) {
                        is Resource.Success<Token> -> {
                            val cryptoManager = TokenCryptoManager()
                            val file = File(filesDir, "token.txt")
                            if(!file.exists()) {
                                file.createNewFile()
                            }
                            cryptoManager.encrypt(result.data!!.accessToken.encodeToByteArray(),result.data.refreshToken.encodeToByteArray(), FileOutputStream(file))
                            state = state.copy(receivedTokens = true);
                        }
                        is Resource.Error -> {
                            Log.i("ERROR", "ERROR WHILE LOGIN")
                        }
                    }
                }
            }
            is LogInAction.TypeInPassword -> {
                state = state.copy(password = action.password)
            }
            is LogInAction.TypeInEmail -> {
                state = state.copy(email = action.email)
            }
        }
    }

}