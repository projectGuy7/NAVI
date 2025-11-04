package com.example.navi.presentation.viewmodels.loginViewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navi.data.remote.dto.UserDTO
import com.example.navi.domain.navi.Token
import com.example.navi.domain.repository.LoginRepository
import com.example.navi.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val repository: LoginRepository): ViewModel() {
    var loginState by mutableStateOf(LoginState(
        firstName = "String",
        lastName = "String",
        age = 18,
        email = "user@example.com",
        password = "strongst",
        isVolunteer = true,
        problems = "String"
    ))
        private set

    fun onEvent(loginAction: LoginAction) {
        when(loginAction) {
            LoginAction.ChangeRole -> {
                loginState = loginState.copy(isVolunteer = !loginState.isVolunteer)
            }
            is LoginAction.ChooseYearOfBirth -> {
                loginState = loginState.copy(age = (Calendar.getInstance().get(Calendar.YEAR) - loginAction.yearOfBirth).toByte())
            }
            is LoginAction.TypeInEmail -> {
                loginState = loginState.copy(email = loginAction.email)
            }
            is LoginAction.TypeInFirstName -> {
                loginState = loginState.copy(firstName = loginAction.firstName)
            }
            is LoginAction.TypeInLastName -> {
                loginState = loginState.copy(lastName = loginAction.lastName)
            }
            is LoginAction.TypeInPassword -> {
                loginState = loginState.copy(password = loginAction.password)
            }
            is LoginAction.TypeInProblems -> {
                loginState = loginState.copy(problems = loginAction.problems)
            }
            LoginAction.CreateUser -> {
                viewModelScope.launch {
                    when(val result = repository.register(
                        UserDTO(
                            firstName = loginState.firstName,
                            lastName = loginState.lastName,
                            age = loginState.age,
                            email = loginState.email,
                            password = loginState.password,
                            role = if(loginState.isVolunteer) "volunteer" else "needy",
                            problems = loginState.problems
                        )
                    )) {
                        is Resource.Error<*> -> {
                            Log.i("ERROR", "ERROR WHILE REGISTERING")
                        }
                        is Resource.Success<*> -> {
                            Log.i("SUCCESS", "REGISTERED SUCCESSFULLY")
                            loginState = loginState.copy(sentVerificationCode = true)
                        }
                    }

                }
            }
            is LoginAction.TypeInVerificationCode -> {
                loginState = loginState.copy(verificationCode = loginAction.verificationCode)
            }
            LoginAction.VerifyCode -> {
                viewModelScope.launch {
                    when(val result = repository.verifyCode(email = loginState.email, code = loginState.verificationCode)) {
                        is Resource.Error<*> -> {
                            Log.i("ERROR", "ERROR WHILE EMAIL VERIFICATION")
                        }
                        is Resource.Success<Token> -> {
                            Log.i("SUCCESS", "REFRESH")
                            loginState = loginState.copy(
                                refreshToken = result.data?.refreshToken,
                                accessToken = result.data?.accessToken
                            )
                        }
                    }
                }
            }
        }
    }
}