package com.dicoding.acnescan.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.acnescan.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay 2 detik untuk tampilan splash screen
        lifecycleScope.launch {
            delay(2000)  // Delay selama 2 detik
            // Pastikan untuk start MainActivity setelah delay
            val intent = Intent(this@Splash, MainActivity::class.java)
            startActivity(intent)
            finish()  // Menutup activity Splash agar tidak bisa kembali ke Splash
        }
    }
}