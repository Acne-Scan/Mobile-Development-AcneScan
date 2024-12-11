package com.dicoding.acnescan.ui.camera

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dicoding.acnescan.data.model.response.Product
import com.dicoding.acnescan.databinding.ActivityResultBinding
import com.dicoding.acnescan.ui.LoadingActivity
import com.dicoding.acnescan.ui.login.LoginActivity
import com.dicoding.acnescan.util.SharedPrefUtil.getToken

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan data dari Intent
        val descRecommendation = intent.getStringExtra(EXTRA_DESC_RECOMMENDATIONS) ?: "No Recommendation"
        val predictionType = intent.getStringExtra(EXTRA_PREDICTION_TYPE) ?: "Unknown Type"
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
        binding.descRecommendations.text = descRecommendation

        // Mendapatkan data produk (dari response server)
        val productImages = intent.getSerializableExtra(EXTRA_PRODUCT_IMAGES) as? Map<*, *>
        val productLinks = intent.getSerializableExtra(EXTRA_PRODUCT_LINKS) as? Map<*, *>

        // Log untuk memeriksa data yang diterima
        Log.d("ResultActivity", "Product images: $productImages")
        Log.d("ResultActivity", "Product Links: $productLinks")

        // Proses data produk
        if (productImages.isNullOrEmpty() || productLinks.isNullOrEmpty()) {
            // Menampilkan pesan error jika tidak ada data yang diterima
            Log.e("ResultActivity", "Product images or product links are empty")
            Toast.makeText(this, "No products available", Toast.LENGTH_SHORT).show()
        } else {
            // Lanjutkan dengan memproses data produk
            val products = productImages.mapNotNull { (productName, imageUrl) ->
                val productUrl = productLinks[productName]
                // Cek apakah productUrl ada, jika tidak kita abaikan produk ini
                if (productUrl != null) {
                    Product(productName.toString(), imageUrl.toString(), productUrl.toString())
                } else {
                    null
                }
            }

            if (products.isNotEmpty()) {
                setupProductRecyclerView(products)
            } else {
                Toast.makeText(this, "No valid products found", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol kembali untuk kembali ke activity sebelumnya
        binding.buttonBack.setOnClickListener {
            onBackPressed()
        }

        // Tombol untuk save
        binding.buttonSave.setOnClickListener {
            // Periksa apakah token login ada
            val token = getToken(this) // Gantilah dengan method untuk mendapatkan token
            Log.d("ResultActivity", "Token: $token")

            if (!token.isNullOrEmpty()) {
                // Jika ada token, arahkan ke halaman loading untuk menyimpan
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Save History")
                    .setMessage("We only can save the type and the recommendation. Do you want to save it?")
                    .setPositiveButton("Yes") { _, _ ->
                        // Jika ya, arahkan ke LoadingActivity
                        val intent = Intent(this, LoadingActivity::class.java).apply {
                            putExtra(LoadingActivity.EXTRA_PREDICTION_TYPE, predictionType) // Mengirim Tipe
                            putExtra(LoadingActivity.EXTRA_IMAGE_URI, imageUriString) // Mengirim URI file gambar
                            putExtra(LoadingActivity.EXTRA_DESC_RECOMMENDATIONS, descRecommendation) // Mengirim rekomendasi
                            putExtra(LoadingActivity.EXTRA_PRODUCT_IMAGES, productImages?.let { HashMap(it) }) // Mengirimkan Map sebagai HashMap
                            putExtra(LoadingActivity.EXTRA_PRODUCT_LINKS, productLinks?.let { HashMap(it) }) // Mengirim Link product
                        }
                        startActivity(intent)
                        finish() // Menutup activity saat ini
                    }
                    .setNegativeButton("No", null)
                    .create()

                dialog.show()
            } else {
                // Jika token tidak ada, tampilkan AlertDialog untuk login
                showLoginDialog()
            }
        }

        // Mengambil data gambar dari Map<String, String> jika ada
        val imageMap = intent.getSerializableExtra(EXTRA_IMAGE_MAP) as? Map<*, *>
        imageMap?.let {
            // Ambil URL gambar pertama (atau logika lain jika ada banyak gambar)
            val imageUrl = it.values.firstOrNull()
            if (imageUrl != null) {
                Glide.with(this)
                    .load(imageUrl)
                    .into(binding.imageViewPlaceholder)
            } else {
                Log.e("ResultActivity", "No image URL found in the map")
                Toast.makeText(this, "No Image URL found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupProductRecyclerView(products: List<Product>) {
        // Setup RecyclerView dengan Adapter
        val adapter = ProductMLAdapter(products)
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProducts.adapter = adapter
    }

    // Fungsi untuk menampilkan AlertDialog yang mengingatkan pengguna untuk login
    private fun showLoginDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Login Required")
            .setMessage("You need to log in first to save the history")
            .setPositiveButton("Login") { _, _ ->
                // Jika memilih login, arahkan ke LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    companion object {
        const val EXTRA_PREDICTION_TYPE = "extra_prediction_type"
        const val EXTRA_CONFIDENCE_SCORE = "extra_confidence_score"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_PRODUCT_IMAGES = "extra_product_images"
        const val EXTRA_DESC_RECOMMENDATIONS = "extra_desc_recommendations"
        const val EXTRA_PRODUCT_LINKS = "extra_product_links"
        const val EXTRA_IMAGE_MAP = "extra_image_map" // Menambahkan EXTRA_IMAGE_MAP untuk mengirim data Map
    }
}