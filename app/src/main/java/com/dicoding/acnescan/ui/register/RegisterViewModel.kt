package com.dicoding.acnescan.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.acnescan.data.factory.Repository
import com.dicoding.acnescan.data.model.response.RegisterRequest
import com.dicoding.acnescan.data.model.response.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel(private val repository: Repository) : ViewModel() {

    // LiveData untuk status registrasi
    private val _registerStatus = MutableLiveData<RegisterStatus>()
    val registerStatus: LiveData<RegisterStatus> = _registerStatus

    // Fungsi untuk registrasi
    fun register(username: String, password: String) {
        val registerRequest = RegisterRequest(username, password)

        viewModelScope.launch {
            try {
                val response: Response<RegisterResponse> = repository.register(registerRequest)

                if (response.isSuccessful) {
                    // Jika registrasi berhasil
                    val registerResponse = response.body()
                    if (registerResponse != null) {
                        // Kirim status sukses ke LiveData
                        _registerStatus.value = RegisterStatus.Success(
                            registerResponse.code ?: -1,
                            registerResponse.message ?: "Success"
                        )
                    } else {
                        _registerStatus.value = RegisterStatus.Failure("Empty response")
                    }
                } else {
                    // Jika request gagal
                    _registerStatus.value = RegisterStatus.Failure("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                // Tangani exception jika terjadi kesalahan
                _registerStatus.value = RegisterStatus.Error("Exception: ${e.message}")
            }
        }
    }

    // Status registrasi yang akan diamati oleh UI
    sealed class RegisterStatus {
        data class Success(val code: Int, val message: String) : RegisterStatus()
        data class Failure(val message: String) : RegisterStatus()
        data class Error(val message: String) : RegisterStatus()
    }
}