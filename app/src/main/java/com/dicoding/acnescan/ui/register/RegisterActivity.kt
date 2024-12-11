package com.dicoding.acnescan.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.acnescan.data.factory.ViewModelFactory
import com.dicoding.acnescan.databinding.ActivityRegisterBinding
import com.dicoding.acnescan.ui.login.LoginActivity
import com.dicoding.acnescan.ui.login.MainActivity
import android.widget.Toast
import com.dicoding.acnescan.ui.register.RegisterViewModel.RegisterStatus

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding dengan layout yang sesuai
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ViewModel dengan ViewModelFactory
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(applicationContext)
        )[RegisterViewModel ::class.java]

        // Set up click listener untuk tombol registrasi
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            val termsChecked = binding.cbTerms.isChecked

            // Cek jika username, password, dan konfirmasi password valid sebelum memulai registrasi
            if (username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password != confirmPassword) {
                    // Jika password dan konfirmasi password tidak cocok, beri tahu pengguna
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                } else if (!termsChecked) {
                    // Jika checkbox ketentuan layanan belum dicentang
                    Toast.makeText(this, "You must agree to the terms and conditions", Toast.LENGTH_SHORT).show()
                } else {
                    // Lakukan registrasi
                    registerViewModel.register(username, password)
                }
            } else {
                // Jika username atau password kosong, beri tahu pengguna
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol Masuk (Anda bisa menambah action disini, misalnya pindah ke LoginActivity)
        binding.tvLogin.setOnClickListener {
            // Handle login button click
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Clear back stack dari LoginActivity
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            // Kembali ke MainActivity, pastikan tidak ada back stack yang menumpuk
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Clear back stack dari LoginActivity
            startActivity(intent)
            finish()
        }

        // Observe status registrasi
        registerViewModel.registerStatus.observe(this) { status ->
            when (status) {
                is RegisterStatus.Success -> {
                    // Jika registrasi sukses, tampilkan pesan sukses
                    Toast.makeText(
                        this,
                        "Registration Successful: ${status.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d(
                        "RegisterActivity",
                        "Registrasi berhasil: ${status.message}"
                    ) // Log debugging

                    // Pindah ke LoginActivity setelah registrasi sukses
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // Pastikan agar pengguna tidak bisa kembali ke RegisterActivity
                }

                is RegisterStatus.Failure -> {
                    // Jika gagal, tampilkan pesan kesalahan
                    Toast.makeText(
                        this,
                        "Registration Failed: ${status.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d(
                        "RegisterActivity",
                        "Registrasi gagal: ${status.message}"
                    ) // Log debugging
                }

                is RegisterStatus.Error -> {
                    // Jika terjadi error lain, tampilkan pesan error
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                    Log.d("RegisterActivity", "Error: ${status.message}") // Log debugging
                }
            }
        }
    }
}