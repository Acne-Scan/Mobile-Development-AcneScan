package com.dicoding.acnescan.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.acnescan.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding dengan layout yang sesuai
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol Login (Anda bisa menambah action disini, misalnya validasi login)
        binding.btnLogin.setOnClickListener {
            // Handle login button click
        }

        // Tombol Kembali (btnBack) untuk kembali ke MainActivity
        binding.btnBack.setOnClickListener {
            // Kembali ke MainActivity, pastikan tidak ada back stack yang menumpuk
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Clear back stack dari LoginActivity
            startActivity(intent)
            finish()
        }
    }
}
