package com.schoolsystem.androidapp.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schoolsystem.androidapp.data.ParentLoginRequest
import com.schoolsystem.androidapp.data.ParentLoginResponse
import com.schoolsystem.androidapp.data.ParentSignupRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

private const val backendBaseUrl = "http://10.0.2.2:9080"

class AuthViewModel : ViewModel() {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(request: ParentLoginRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response: ParentLoginResponse = client.post("$backendBaseUrl/api/auth/login") {
                    setBody(request)
                }.body()
                _uiState.update { it.copy(isLoading = false, loginResult = response, session = response) }
            } catch (cause: Exception) {
                _uiState.update { it.copy(isLoading = false, error = cause.message ?: "Login failed") }
            }
        }
    }

    fun signup(request: ParentSignupRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, signupSuccess = false) }
            try {
                client.post("$backendBaseUrl/api/auth/signup") {
                    setBody(request)
                }
                _uiState.update { it.copy(isLoading = false, signupSuccess = true) }
            } catch (cause: Exception) {
                _uiState.update { it.copy(isLoading = false, error = cause.message ?: "Signup failed") }
            }
        }
    }

    fun clearLoginResult() {
        _uiState.update { it.copy(loginResult = null) }
    }

    fun logout() {
        _uiState.update { it.copy(session = null, loginResult = null) }
    }

    override fun onCleared() {
        client.close()
        super.onCleared()
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginResult: ParentLoginResponse? = null,
    val signupSuccess: Boolean = false,
    val session: ParentLoginResponse? = null
)
