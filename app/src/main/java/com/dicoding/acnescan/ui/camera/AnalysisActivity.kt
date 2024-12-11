package com.dicoding.acnescan.ui.camera

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.acnescan.data.model.response.ImageRequest
import com.dicoding.acnescan.data.model.retrofit.ApiConfigML
import com.dicoding.acnescan.databinding.ActivityAnalysisBinding
import com.dicoding.acnescan.data.utils.convertImageToBase64
import com.dicoding.acnescan.data.utils.createCustomTempFile
import com.dicoding.acnescan.data.utils.reduceFileImage
import com.dicoding.acnescan.data.utils.toSafeFloat
import com.dicoding.acnescan.data.utils.uriToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.SocketTimeoutException

class AnalysisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalysisBinding
    private var bitmapToAnalyze: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding object
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val imagePath = intent.getStringExtra(EXTRA_IMAGE_PATH)
        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)

        if (imagePath != null) {
            // Gambar dari CameraFragment atau HistoryFragment (path)
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                // Mengurangi ukuran gambar
                val reducedImageFile = imageFile.reduceFileImage()

                // Kemudian, Anda dapat memproses file gambar yang lebih kecil ini
                val bitmap = BitmapFactory.decodeFile(reducedImageFile.path)
                bitmapToAnalyze = bitmap
                binding.imageView.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show()
            }
        } else if (imageUri != null) {
            // Gambar dari galeri (URI)
            val imageFile = uriToFile(Uri.parse(imageUri), this) // Mengonversi URI menjadi file
            if (imageFile.exists()) {
                // Mengurangi ukuran gambar
                val reducedImageFile = imageFile.reduceFileImage()

                // Membaca gambar dengan ukuran yang sudah diperkecil
                val bitmap = BitmapFactory.decodeFile(reducedImageFile.path)
                bitmapToAnalyze = bitmap
                binding.imageView.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol analisis
        binding.buttonAnalyze.setOnClickListener {
            // Menghilangkan tampilan semuanya dan menampilkan loading
            binding.imageView.visibility = View.GONE
            binding.buttonContainer.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            bitmapToAnalyze?.let {
                // Menampilkan Toast "Harap menunggu"
                Toast.makeText(this, "Please wait your image is on processing...", Toast.LENGTH_SHORT).show()

                // Memulai proses analisis gambar
                analyzeImage(it)
            } ?: Toast.makeText(this, "No image to analyze", Toast.LENGTH_SHORT).show()
        }

        // Tombol kembali
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    // Fungsi untuk menganalisis gambar setelah di-upload
    private fun analyzeImage(bitmap: Bitmap) {
        // Mengonversi Bitmap ke File sementara untuk diupload
        val tempFile = createCustomTempFile(this)
        val outputStream = tempFile.outputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        // Mengurangi ukuran gambar
        val reducedFile = tempFile.reduceFileImage()

        // Mengonversi gambar menjadi base64
        val base64Image = convertImageToBase64(reducedFile)

        // Membuat objek ImageRequest dengan base64 image
        val imageRequest = ImageRequest(base64Image)

        // Menyiapkan request body dengan ImageRequest
        val apiService = ApiConfigML.getApiServiceML()

        // Menjalankan proses analisis di dalam coroutine
        GlobalScope.launch(Dispatchers.Main) {
            try {
                Log.d("AnalysisActivity", "Starting image upload process...")

                // Memanggil API untuk upload gambar dalam format base64 dan mendapatkan hasil prediksi
                val response = apiService.uploadImage(imageRequest)

                // Debugging: Log response code dan body untuk mempermudah analisis
                Log.d("AnalysisActivity", "Response code: ${response.code()}")
                if (response.isSuccessful) {
                    Log.d("AnalysisActivity", "Server response: SUCCESS")
                    val responseBody = response.body()
                    if (responseBody != null) {
                        // Menangani response dari server yang valid
                        val predictedClass = responseBody.prediction ?: "Unknown Type"
                        val confidence = responseBody.confidence?.toSafeFloat() ?: 0f // Menggunakan extension function
                        val recommendations = responseBody.recommendation ?: "No Recommendation"
                        val productLinks = responseBody.productLinks ?: "No Product Links"

                        // Log hasil prediksi
                        Log.d("AnalysisActivity", "Predicted Class: $predictedClass, Confidence: $confidence")

                        // Kirim hasil ke ResultActivity
                        sendAnalysisResult(predictedClass,
                            confidence,
                            responseBody.productImages,
                            recommendations,
                            responseBody.productLinks
                        )

                    } else {
                        // Jika response body kosong
                        Log.e("AnalysisActivity", "Error: Server returned an empty response body.")
                        Toast.makeText(this@AnalysisActivity, "Error: Server returned an empty response", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Menangani error response
                    Log.e("AnalysisActivity", "Error Response: Code: ${response.code()} - Message: ${response.message()}")
                    Toast.makeText(this@AnalysisActivity, "Error: Server returned an error", Toast.LENGTH_SHORT).show()
                }
            } catch (e: SocketTimeoutException) {
                // Menangani timeout error
                Log.e("AnalysisActivity", "Timeout Error: Gagal menghubungi server", e)
                Toast.makeText(this@AnalysisActivity, "Timeout: Gagal menghubungi server", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                // Menangani kesalahan jaringan lainnya (misalnya jaringan terputus)
                Log.e("AnalysisActivity", "Network Error: ${e.message}", e)
                Toast.makeText(this@AnalysisActivity, "Network error", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                if (e is HttpException && e.code() == 500) {
                    Log.e("AnalysisActivity", "Server Error: ${e.message}")
                    Toast.makeText(this@AnalysisActivity, "Server Error: There is a problem with the server try again later.", Toast.LENGTH_SHORT).show()
                } else {
                    // Penanganan kesalahan lain
                    Log.e("AnalysisActivity", "General Error: ${e.message}", e)
                    Toast.makeText(this@AnalysisActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveCompressedBitmapToFile(bitmap: Bitmap): File {
        val file = File(cacheDir, "compressed_image.jpg") // Menyimpan di direktori cache aplikasi
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream) // Kompresi gambar dengan kualitas 80
        fileOutputStream.flush()
        fileOutputStream.close()
        return file
    }

    private fun sendAnalysisResult(
        predictionType: String,
        confidenceScore: Float,
        productImages: Map<String, String>?,
        descRecommendation: String,
        productLinks: Map<String, String>? ) {

        // Simpan gambar yang dikompresi ke file sementara
        val reducedImageFile = saveCompressedBitmapToFile(bitmapToAnalyze!!)

        // Mengambil URI file gambar
        val imageUri = Uri.fromFile(reducedImageFile)


        // Membuat intent untuk ResultActivity
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_PREDICTION_TYPE, predictionType) // Mengirim Tipe
            putExtra(ResultActivity.EXTRA_CONFIDENCE_SCORE, confidenceScore) // Mengirim skor
            putExtra(ResultActivity.EXTRA_IMAGE_URI, imageUri.toString()) // Mengirim URI file gambar
            putExtra(ResultActivity.EXTRA_PRODUCT_IMAGES, productImages?.let { HashMap(it) }) // Mengirimkan Map sebagai HashMap
            putExtra(ResultActivity.EXTRA_DESC_RECOMMENDATIONS, descRecommendation) // Mengirim rekomendasi
            putExtra(ResultActivity.EXTRA_PRODUCT_LINKS, productLinks?.let { HashMap(it) }) // Mengirim Link product
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        // Pastikan tampilan kembali ke keadaan default ketika halaman ini dimuat ulang
        binding.progressBar.visibility = View.GONE // Sembunyikan loading bar
        binding.imageView.visibility = View.VISIBLE // Tampilkan kembali gambar
        binding.buttonContainer.visibility = View.VISIBLE // Tampilkan kembali tombol
    }

    companion object {
        const val EXTRA_IMAGE_PATH = "extra_image_path"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}