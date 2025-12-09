package com.example.navi.presentation.viewmodels.signInViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navi.cryptoManager.TokenCryptoManager
import com.example.navi.data.remote.dto.EmailVerificationDTO
import com.example.navi.data.remote.dto.UserDTO
import com.example.navi.di.qualifiers.FilesDir
import com.example.navi.domain.navi.Token
import com.example.navi.domain.repository.LoginRepository
import com.example.navi.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(val repository: LoginRepository, @param:FilesDir val filesDir: File): ViewModel() {
    var state by mutableStateOf(SignInState(
        firstName = "Azamat",
        lastName = "Bolatkhanov",
        age = 18,
        email = "bolathanovazamat047@gmail.com",
        password = "Azamat1234@",
        isVolunteer = true,
        problems = "String"
    ))
        private set

    fun onEvent(loginAction: SignInAction) {
        when(loginAction) {
            SignInAction.ChangeRole -> {
                state = state.copy(isVolunteer = !state.isVolunteer)
            }
            is SignInAction.ChooseYearOfBirth -> {
                state = state.copy(age = (Calendar.getInstance().get(Calendar.YEAR) - loginAction.yearOfBirth).toByte())
            }
            is SignInAction.TypeInEmail -> {
                state = state.copy(email = loginAction.email)
            }
            is SignInAction.TypeInFirstName -> {
                state = state.copy(firstName = loginAction.firstName)
            }
            is SignInAction.TypeInLastName -> {
                state = state.copy(lastName = loginAction.lastName)
            }
            is SignInAction.TypeInPassword -> {
                state = state.copy(password = loginAction.password)
            }
            is SignInAction.TypeInProblems -> {
                state = state.copy(problems = loginAction.problems)
            }
            SignInAction.CreateUser -> {
                viewModelScope.launch {
                    when(val result = repository.register(
                        UserDTO(
                            firstName = state.firstName,
                            lastName = state.lastName,
                            age = state.age,
                            email = state.email,
                            password = state.password,
                            role = if(state.isVolunteer) "volunteer" else "needy",
                            problems = state.problems
                        )
                    )) {
                        is Resource.Error<*> -> {
                            Log.i("ERROR", "ERROR WHILE REGISTERING")
                        }
                        is Resource.Success<*> -> {
                            Log.i("SUCCESS", "REGISTERED SUCCESSFULLY")
                            state = state.copy(sentVerificationCode = true)
                        }
                    }

                }
            }
            is SignInAction.TypeInVerificationCode -> {
                state = state.copy(verificationCode = loginAction.verificationCode)
            }
            SignInAction.VerifyCode -> {
                viewModelScope.launch {
                    when(val result = repository.verifyCode(EmailVerificationDTO(
                        email = state.email,
                        verificationCode = state.verificationCode
                    ))) {
                        is Resource.Error<*> -> {
                            Log.i("ERROR", "ERROR WHILE EMAIL VERIFICATION")
                        }
                        is Resource.Success<Token> -> {
                            val cryptoManager = TokenCryptoManager()
                            val file = File(filesDir, "token.txt")
                            if(!file.exists()) {
                                file.createNewFile()
                            }
                            cryptoManager.encrypt(result.data!!.accessToken.encodeToByteArray(),result.data.refreshToken.encodeToByteArray(), FileOutputStream(file))
                            state = state.copy(receivedTokens = true);
                        }
                    }
                }
            }
        }
    }
}