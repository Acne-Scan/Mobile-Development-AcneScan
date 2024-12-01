package com.dicoding.acnescan.ui.camera

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.acnescan.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan data dari intent
        val predictionType = intent.getStringExtra(EXTRA_PREDICTION_TYPE) ?: "Tidak diketahui"
        val confidenceScore = intent.getFloatExtra(EXTRA_CONFIDENCE_SCORE, 0f)

        // Menampilkan hasil analisis di TextViews
        binding.predictionTitle.text = "$predictionType"
        binding.confidenceScore.text = "Confidence Score: ${(confidenceScore * 100).toInt()}%"

        // binding.rvRecommendation.adapter = RecommendationAdapter()

        // Menerima gambar dalam bentuk ByteArray
        val byteArray = intent.getByteArrayExtra(EXTRA_IMAGE)
        byteArray?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.imageViewPlaceholder.setImageBitmap(bitmap)
        } ?: Toast.makeText(this, "Gambar tidak ditemukan", Toast.LENGTH_SHORT).show()

        // Tombol kembali
        binding.buttonBack.setOnClickListener {
            onBackPressed() // Mengembalikan ke activity sebelumnya
        }
    }

    companion object {
        const val EXTRA_PREDICTION_TYPE = "extra_prediction_type"
        const val EXTRA_CONFIDENCE_SCORE = "extra_confidence_score"
        const val EXTRA_IMAGE = "extra_image" // Key untuk gambar dalam bentuk ByteArray
    }
}