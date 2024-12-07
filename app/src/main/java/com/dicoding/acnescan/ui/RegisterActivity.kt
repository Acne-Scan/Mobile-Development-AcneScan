package com.dicoding.acnescan.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.acnescan.databinding.ActivityRegisterBinding
import com.dicoding.acnescan.ui.login.LoginActivity
import com.dicoding.acnescan.ui.login.MainActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding dengan layout yang sesuai
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol Register (Anda bisa menambah action disini, misalnya pindah ke RegisterActivity)
        binding.btnRegister.setOnClickListener {
            // Handle register button click
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
    }
}