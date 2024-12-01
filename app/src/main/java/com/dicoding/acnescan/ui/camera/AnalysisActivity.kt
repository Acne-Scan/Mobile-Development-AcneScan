package com.dicoding.acnescan.ui.camera

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import com.dicoding.acnescan.data.retrofit.ApiConfigML
import com.dicoding.acnescan.data.retrofit.ImageRequest
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
import java.io.ByteArrayOutputStream
import java.io.File
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
                val bitmap = correctImageOrientation(imageFile)
                bitmapToAnalyze = bitmap
                binding.imageView.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show()
            }
        } else if (imageUri != null) {
            // Gambar dari galeri
            val uri = Uri.parse(imageUri)
            val imageFile = uriToFile(uri, this).reduceFileImage()  // Menggunakan fungsi di FileUtils
            bitmapToAnalyze = BitmapFactory.decodeFile(imageFile.absolutePath)
            binding.imageView.setImageBitmap(bitmapToAnalyze)
        } else {
            Toast.makeText(this, "No image provided", Toast.LENGTH_SHORT).show()
        }

        // Tombol analisis
        binding.buttonAnalyze.setOnClickListener {
            bitmapToAnalyze?.let {
                // Menampilkan Toast "Harap menunggu"
                Toast.makeText(this, "Harap menunggu gambar sedang diproses ...", Toast.LENGTH_SHORT).show()

                // Memulai proses analisis gambar
                analyzeImage(it)
            } ?: Toast.makeText(this, "No image to analyze", Toast.LENGTH_SHORT).show()
        }

        // Tombol kembali
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun correctImageOrientation(imageFile: File): Bitmap {
        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
        val exif = ExifInterface(imageFile)
        val isFrontCamera = intent.getBooleanExtra(EXTRA_CAMERA_FACING, true)

        // Mendapatkan orientasi EXIF
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        // Melakukan rotasi berdasarkan orientasi EXIF
        val rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
            else -> bitmap
        }

        return rotatedBitmap
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
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
        val apiService = ApiConfigML.getApiService()

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
                        // Menangani response yang valid
                        val predictedClass = responseBody.prediction ?: "Unknown"
                        val confidence = responseBody.confidence?.toSafeFloat() ?: 0f // Menggunakan extension function

                        // Log hasil prediksi
                        Log.d("AnalysisActivity", "Predicted Class: $predictedClass, Confidence: $confidence")

                        // Menampilkan hasil prediksi
                        val resultMessage = "Prediksi: $predictedClass\nKepercayaan: ${String.format("%.2f", confidence * 100)}%"
                        Toast.makeText(this@AnalysisActivity, resultMessage, Toast.LENGTH_LONG).show()

                        // Kirim hasil ke ResultActivity
                        sendAnalysisResult(predictedClass, confidence)
                    } else {
                        // Jika response body kosong
                        Log.e("AnalysisActivity", "Error: Server returned an empty response body.")
                        Toast.makeText(this@AnalysisActivity, "Error: Server returned an empty response", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Menangani error response
                    Log.e("AnalysisActivity", "Error Response: Code: ${response.code()} - Message: ${response.message()}")
                    Toast.makeText(this@AnalysisActivity, "Error: Server returned an error (status code: ${response.code()})", Toast.LENGTH_SHORT).show()
                }
            } catch (e: SocketTimeoutException) {
                // Menangani timeout error
                Log.e("AnalysisActivity", "Timeout Error: Gagal menghubungi server", e)
                Toast.makeText(this@AnalysisActivity, "Timeout: Gagal menghubungi server", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                // Menangani kesalahan jaringan lainnya (misalnya jaringan terputus)
                Log.e("AnalysisActivity", "Network Error: ${e.message}", e)
                Toast.makeText(this@AnalysisActivity, "Kesalahan jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                if (e is HttpException && e.code() == 500) {
                    Log.e("AnalysisActivity", "Server Error: ${e.message}")
                    Toast.makeText(this@AnalysisActivity, "Server Error: Terjadi kesalahan di server, coba lagi nanti.", Toast.LENGTH_SHORT).show()
                } else {
                    // Penanganan kesalahan lain
                    Log.e("AnalysisActivity", "General Error: ${e.message}", e)
                    Toast.makeText(this@AnalysisActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun compressBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream) // Kompresi ke JPEG dengan kualitas 80
        return byteArrayOutputStream.toByteArray()
    }

    private fun sendAnalysisResult(predictionType: String, confidenceScore: Float) {
        // Mengompresi gambar menjadi ByteArray
        val byteArray = compressBitmapToByteArray(bitmapToAnalyze!!)

        // Membuat intent untuk ResultActivity
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_PREDICTION_TYPE, predictionType)
            putExtra(ResultActivity.EXTRA_CONFIDENCE_SCORE, confidenceScore)
            putExtra(ResultActivity.EXTRA_IMAGE, byteArray) // Mengirim gambar sebagai ByteArray
        }
        startActivity(intent)
    }

    companion object {
        const val EXTRA_IMAGE_PATH = "extra_image_path"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_CAMERA_FACING = "extra_camera_facing"
    }
}