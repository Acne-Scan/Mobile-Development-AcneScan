package com.dicoding.acnescan.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.acnescan.R
import com.dicoding.acnescan.data.model.response.AddHistoryRequest
import com.dicoding.acnescan.data.model.retrofit.ApiConfig
import com.dicoding.acnescan.data.model.response.AddHistoryResponse
import com.dicoding.acnescan.util.SharedPrefUtil.getToken
import kotlinx.coroutines.launch
import retrofit2.Response

class LoadingActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        progressBar = findViewById(R.id.progressBar)

        // Ambil data dari Intent
        val prediction = intent.getStringExtra(EXTRA_PREDICTION_TYPE) ?: "No Prediction"
        val recommendation = intent.getStringExtra(EXTRA_DESC_RECOMMENDATIONS) ?: "No Recommendation"
        val user_picture = intent.getStringExtra(EXTRA_IMAGE_URI) ?: "No Image"
        val product_links = intent.getSerializableExtra(EXTRA_PRODUCT_LINKS) as Map<String, String>
        val image = intent.getSerializableExtra(EXTRA_PRODUCT_IMAGES) as Map<String, String>

        // Menampilkan data untuk debugging
        Log.d("LoadingActivity", "Prediction: $prediction")
        Log.d("LoadingActivity", "Recommendation: $recommendation")
        Log.d("LoadingActivity", "Image Map: $user_picture")
        Log.d("LoadingActivity", "Product Links: $product_links")
        Log.d("LoadingActivity", "Product Image: $image")

        // Kirim data ke API
        sendDataToApi(prediction, recommendation, user_picture, product_links, image)
    }

    private fun sendDataToApi(prediction: String, recommendation: String, user_picture: String, product_links: Map<String, String>, image: Map<String, String>) {
        // Set progress bar visible while loading
        progressBar.visibility = View.VISIBLE

        // Ambil token dari SharedPreferences
        val token = getToken(this)

        if (!token.isNullOrEmpty()) {
            // Setup data untuk API request
            val apiRequest = AddHistoryRequest(
                prediction = prediction,
                recommendation = recommendation,
                user_picture = user_picture,
                product_links = product_links,
                image = image
            )

            // Menggunakan ApiConfig untuk mendapatkan ApiService
            val apiService = ApiConfig.getApiService(applicationContext)

            lifecycleScope.launch {
                try {
                    // Lakukan panggilan API untuk mengirim data
                    val response: Response<AddHistoryResponse> = apiService.saveHistory(apiRequest)

                    if (response.isSuccessful) {
                        // Proses data yang diterima jika berhasil
                        handleSuccessResponse(response)
                    } else {
                        // Tampilkan pesan error jika gagal
                        handleErrorResponse(response)
                    }
                } catch (e: Exception) {
                    // Tangani error jaringan atau lainnya
                    handleError(e)
                }
            }
        } else {
            // Jika token tidak ada, tampilkan pesan kesalahan
            Log.e("LoadingActivity", "Token not found!")
            progressBar.visibility = View.GONE
        }
    }

    private fun handleSuccessResponse(response: Response<AddHistoryResponse>) {
        progressBar.visibility = View.GONE
        // Tampilkan hasil atau pindah ke activity lain jika perlu
        Log.d("LoadingActivity", "Success: ${response.body()?.message}")
        Log.d("LoadingActivity", "Body: ${response.body()}")

        val intent = Intent(this, BottomNavigation::class.java)
        startActivity(intent)
        finish()
    }

    private fun handleErrorResponse(response: Response<AddHistoryResponse>) {
        progressBar.visibility = View.GONE
        // Tampilkan pesan error jika request gagal
        Log.e("LoadingActivity", "Error: ${response.message()}")
    }

    private fun handleError(e: Exception) {
        progressBar.visibility = View.GONE
        Log.e("LoadingActivity", "Error: ${e.message}")
    }

    companion object {
        const val EXTRA_PREDICTION_TYPE = "extra_prediction"
        const val EXTRA_DESC_RECOMMENDATIONS = "extra_desc_recommendation"
        const val EXTRA_IMAGE_URI = "extra_image_map"
        const val EXTRA_PRODUCT_LINKS = "extra_product_links"
        const val EXTRA_PRODUCT_IMAGES = "extra_product_images"
    }
}