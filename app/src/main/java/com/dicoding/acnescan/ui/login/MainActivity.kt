package com.dicoding.acnescan.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.acnescan.R
import com.dicoding.acnescan.ui.BottomNavigation

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLoginWithoutAccount: Button = findViewById(R.id.btnLoginWithoutAccount)
        btnLoginWithoutAccount.setOnClickListener {
            Log.d("MainActivity", "Button clicked")
            val intent = Intent(this, BottomNavigation::class.java)
            startActivity(intent)
            finish()
        }
    }
}
