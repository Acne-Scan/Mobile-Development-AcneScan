package com.dicoding.acnescan.ui.camera

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import com.dicoding.acnescan.R
import com.dicoding.acnescan.helper.ImageClassifierHelper
import java.io.File
import java.io.FileInputStream

class AnalysisActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var bitmapToAnalyze: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        // Menghubungkan ImageView dari layout
        imageView = findViewById(R.id.image_view)

        // Ambil data dari Intent
        val imagePath = intent.getStringExtra(EXTRA_IMAGE_PATH)
        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)

        if (imagePath != null) {
            // Gambar dari kamera
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                val bitmap = correctImageOrientation(imageFile)
                bitmapToAnalyze = bitmap
                imageView.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show()
            }
        } else if (imageUri != null) {
            // Gambar dari galeri
            val uri = Uri.parse(imageUri)
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            bitmapToAnalyze = bitmap
            imageView.setImageBitmap(bitmap)
        } else {
            Toast.makeText(this, "No image provided", Toast.LENGTH_SHORT).show()
        }

        // Tombol analisis
        findViewById<Button>(R.id.button_analyze).setOnClickListener {
            bitmapToAnalyze?.let {
                analyzeImage(it)
            } ?: Toast.makeText(this, "No image to analyze", Toast.LENGTH_SHORT).show()
        }

        // Tombol kembali
        findViewById<Button>(R.id.button_back).setOnClickListener {
            finish()
        }
    }

    private fun correctImageOrientation(imageFile: File): Bitmap {
        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
        val exif = ExifInterface(FileInputStream(imageFile))

        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        val rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }

        // Jika kamera depan (front camera), lakukan pembalikan horizontal
        return flipBitmapIfNeeded(rotatedBitmap)
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun flipBitmapIfNeeded(bitmap: Bitmap): Bitmap {
        // Lakukan flip horizontal jika gambar berasal dari kamera depan
        val matrix = Matrix()
        matrix.preScale(-1f, 1f) // Pembalikan horizontal
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun analyzeImage(bitmap: Bitmap) {
        val classifier = ImageClassifierHelper(this) // Pastikan helper sudah dikonfigurasi
        try {
            // Output buffer untuk hasil prediksi, disesuaikan dengan output shape [1, 5]
            val outputBuffer = Array(1) { FloatArray(5) }

            // Jalankan inferensi (pastikan method classifyImage menerima outputBuffer sebagai parameter)
            classifier.classifyImage(bitmap, outputBuffer)

            // Proses hasil prediksi
            val probabilities = outputBuffer[0] // Hasil probabilitas untuk setiap kelas
            val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
            val confidenceScore = probabilities[maxIndex]

            // Daftar nama kelas (sesuaikan dengan model Anda)
            val classNames = arrayOf("Blackheads", "Cyst", "Papules", "Pustules", "Whiteheads")
            val predictionType = if (maxIndex in classNames.indices) classNames[maxIndex] else "Unknown"

            // Kirim hasil analisis ke ResultActivity
            sendAnalysisResult(bitmap, predictionType, confidenceScore)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error during analysis: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            classifier.close()
        }
    }



    private fun sendAnalysisResult(bitmap: Bitmap, predictionType: String, confidenceScore: Float) {
        // Simpan gambar sementara untuk diteruskan ke ResultActivity
        val tempFile = File(cacheDir, "analyzed_image.jpg")
        val outputStream = tempFile.outputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        // Membuat intent untuk ResultActivity
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_IMAGE_PATH, tempFile.absolutePath)
            putExtra(ResultActivity.EXTRA_PREDICTION_TYPE, predictionType)
            putExtra(ResultActivity.EXTRA_CONFIDENCE_SCORE, confidenceScore)
        }
        startActivity(intent)
    }

    companion object {
        const val EXTRA_IMAGE_PATH = "extra_image_path"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}