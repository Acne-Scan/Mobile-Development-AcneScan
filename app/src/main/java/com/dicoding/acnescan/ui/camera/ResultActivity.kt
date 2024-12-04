package com.dicoding.acnescan.ui.camera

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.acnescan.data.model.response.Product
import com.dicoding.acnescan.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan data dari Intent
        val descRecommendation = intent.getStringExtra(EXTRA_DESC_RECOMMENDATIONS) ?: "No Recommendation"
        val predictionType = intent.getStringExtra(EXTRA_PREDICTION_TYPE) ?: "Unknown Type"
        val confidenceScore = intent.getFloatExtra(EXTRA_CONFIDENCE_SCORE, 0f)
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)

        // Menangani URI gambar (jika ada)
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            try {
                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                binding.imageViewPlaceholder.setImageBitmap(bitmap) // Menampilkan gambar di ImageView
            } catch (e: Exception) {
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No Image Found", Toast.LENGTH_SHORT).show()
        }

        // Menampilkan hasil analisis di TextViews
        binding.predictionTitle.text = predictionType
        binding.confidenceScore.text = "Confidence Score: ${(confidenceScore * 100).toInt()}%"
        binding.descRecommendations.text = descRecommendation

        // Mendapatkan data produk (dari response server)
        val productImages = intent.getSerializableExtra(EXTRA_PRODUCT_IMAGES) as? Map<*, *>

        // Jika produk gambar ditemukan, tampilkan di RecyclerView
        if (productImages != null) {
            val products = productImages.map { Product(it.key.toString(), it.value.toString()) }
            setupProductRecyclerView(products)
        } else {
            Toast.makeText(this, "No products found", Toast.LENGTH_SHORT).show()
        }

        // Tombol kembali untuk kembali ke activity sebelumnya
        binding.buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupProductRecyclerView(products: List<Product>) {
        val adapter = ProductImageAdapter(products)
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProducts.adapter = adapter
    }

    companion object {
        const val EXTRA_PREDICTION_TYPE = "extra_prediction_type"
        const val EXTRA_CONFIDENCE_SCORE = "extra_confidence_score"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_PRODUCT_IMAGES = "extra_product_images"
        const val EXTRA_DESC_RECOMMENDATIONS = "extra_desc_recommendations"
    }
}