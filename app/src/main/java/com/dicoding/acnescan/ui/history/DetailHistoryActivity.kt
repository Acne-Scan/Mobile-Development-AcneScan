package com.dicoding.acnescan.ui.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dicoding.acnescan.data.model.response.Product
import com.dicoding.acnescan.databinding.ActivityDetailHistoryBinding
import com.dicoding.acnescan.ui.camera.CameraActivity
import com.dicoding.acnescan.ui.camera.ProductMLAdapter

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve data from the intent
        val predictionType = intent.getStringExtra(EXTRA_PREDICTION_TYPE)
        val userPictureUri = intent.getStringExtra(EXTRA_IMAGE_URI)
        val recommendations = intent.getStringExtra(EXTRA_DESC_RECOMMENDATIONS)
        val productImages = intent.getSerializableExtra(EXTRA_PRODUCT_IMAGES) as? Map<String, String>
        val productLinks = intent.getSerializableExtra(EXTRA_PRODUCT_LINKS) as? Map<String, String>

        // Set the prediction type and recommendations to text views
        binding.predictionTitle.text = predictionType ?: "No prediction"
        binding.descRecommendations.text = recommendations ?: "No recommendations"

        // Log Debugging
        Log.d("DetailHistoryActivity", "Prediction Type: $predictionType")
        Log.d("DetailHistoryActivity", "User Picture URI: $userPictureUri")
        Log.d("DetailHistoryActivity", "Recommendations: $recommendations")

//        // Load the user picture using Glide
//        Glide.with(this)
//            .load(userPictureUri)
//            .into(binding.imageViewPlaceholder)

        // Process products from the productImages map
        val products = productImages?.mapNotNull { (productName, imageUrl) ->
            val productUrl = productLinks?.get(productName)
            // Cek apakah productUrl ada, jika tidak kita abaikan produk ini
            if (productUrl != null) {
                // Create Product object
                Product(productName, imageUrl, productUrl)
            } else {
                null
            }
        }

        // Check if there are no products and show a toast message if empty
        if (products.isNullOrEmpty()) {
            Log.e("DetailHistoryActivity", "No products available")
            Toast.makeText(this, "No products available", Toast.LENGTH_SHORT).show()
        } else {
            // Setup RecyclerView with products data
            setupRecyclerView(products)
        }

        // Tombol untuk ke camera
        binding.notes.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Tombol kembali untuk kembali ke activity sebelumnya
        binding.buttonBack.setOnClickListener {
           onBackPressed()
        }
    }

    private fun setupRecyclerView(products: List<Product>) {
        // Initialize the adapter and pass the list of products
        val adapter = ProductMLAdapter(products)

        // Setup RecyclerView
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProducts.adapter = adapter

        // Log for debugging
        Log.d("DetailHistoryActivity", "RecyclerView setup with ${products.size} products")
    }

    companion object {
        const val EXTRA_PREDICTION_TYPE = "extra_prediction_type"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_DESC_RECOMMENDATIONS = "extra_desc_recommendations"
        const val EXTRA_PRODUCT_IMAGES = "extra_image_map"
        const val EXTRA_PRODUCT_LINKS = "extra_product_links"
    }
}