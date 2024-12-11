package com.dicoding.acnescan.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.acnescan.data.factory.ViewModelFactory
import com.dicoding.acnescan.databinding.ActivityLoginBinding
import com.dicoding.acnescan.data.model.response.LoginRequest
import com.dicoding.acnescan.ui.BottomNavigation
import com.dicoding.acnescan.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding dengan layout yang sesuai
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan ViewModel menggunakan ViewModelFactory
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(applicationContext)
        )[LoginViewModel::class.java]

        // Tombol Login
        binding.btnLogin.setOnClickListener {
            val username = binding.etName.text.toString()
            val password = binding.etPassword.text.toString()

            // Validasi input
            if (username.isNotEmpty() && password.isNotEmpty()) {
                val loginRequest = LoginRequest(username, password)
                loginViewModel.login(loginRequest)
            } else {
                Toast.makeText(this, "Please fill your username and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol Kembali (btnBack) untuk kembali ke MainActivity
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        // Set onClickListener untuk button register
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Clear back stack dari LoginActivity
            startActivity(intent)
        }

        // Observasi status login
        loginViewModel.loginStatus.observe(this) { status ->
            when (status) {
                is LoginViewModel.LoginStatus.Success -> {
                    saveUserData(status.username, status.token) // Menyimpan username dan tokenToken(status.token)
                    Log.d("LoginActivity", "Token berhasil disimpan: ${status.token}") // Log token di Activity
                    Toast.makeText(applicationContext, "Login success", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                }

                is LoginViewModel.LoginStatus.Failure -> {
                    Log.d("LoginActivity", "Login gagal: ${status.message}") // Log message gagal login
                    Toast.makeText(
                        applicationContext,
                        "Failed to login: Please check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is LoginViewModel.LoginStatus.Error -> {
                    Log.e("LoginActivity", "Network error: ${status.message}") // Log error jika ada
                    Toast.makeText(
                        applicationContext,
                        "Network error: Please check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // Menyimpan token ke SharedPreferences (atau tempat yang aman)
    private fun saveUserData(username: String, token: String?) {
        if (!token.isNullOrEmpty()) {
            val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
            sharedPreferences.edit()
                .putString("username", username) // Menyimpan username
                .putString("token", token)       // Menyimpan token
                .apply()
            Log.d("LoginActivity", "Data pengguna disimpan ke SharedPreferences") // Log penyimpanan
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, BottomNavigation::class.java)
        startActivity(intent)
        finish()
    }
}