package com.dicoding.acnescan.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.acnescan.data.factory.Repository
import com.dicoding.acnescan.data.model.response.LoginRequest
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {

    // LiveData untuk status login
    private val _loginStatus = MutableLiveData<LoginStatus>()
    val loginStatus: LiveData<LoginStatus> = _loginStatus

    // Fungsi login yang dipanggil dari UI (LoginActivity)
    fun login(loginRequest: LoginRequest) {

        viewModelScope.launch {
            try {
                val response = repository.login(loginRequest)
                if (response.isSuccessful) {
                    // Jika login berhasil
                    val loginResponse = response.body()
                    if (loginResponse?.code == 200) {
                        val token = loginResponse.data?.token
                        val username = loginResponse.data?.username
                        Log.d("LoginViewModel", "Token berhasil didapatkan: $token")
                        _loginStatus.value = LoginStatus.Success(token ?: "", username ?: "")
                    } else {
                        _loginStatus.value = LoginStatus.Failure(loginResponse?.message ?: "Unknown error")
                    }
                } else {
                    _loginStatus.value = LoginStatus.Error(response.errorBody()?.string() ?: "Unknown error")
                }
            } catch (e: Exception) {
                _loginStatus.value = LoginStatus.Error(e.message ?: "Unknown error")
                Log.e("LoginViewModel", "Error during login: ${e.message}")
            }
        }
    }

    // Status login yang akan diamati oleh UI
    sealed class LoginStatus {
        data class Success(
            val token: String,
            val username: String
        ) : LoginStatus()

        data class Failure(val message: String) : LoginStatus()
        data class Error(val message: String) : LoginStatus()
    }
}
