package com.dicoding.acnescan.ui.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import com.dicoding.acnescan.R
import java.io.File
import java.io.FileInputStream

class ResultActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var resultTypeTextView: TextView
    private lateinit var confidenceTextView: TextView
    private lateinit var backButton: Button // Tombol untuk kembali

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Menghubungkan ImageView dan TextViews dengan layout
        val imageView = findViewById<ImageView>(R.id.image_view)
        val resultTypeTextView = findViewById<TextView>(R.id.analysis_result_type)
        val confidenceTextView = findViewById<TextView>(R.id.analysis_result_confidence)
        val backButton = findViewById<Button>(R.id.button_back) // Tombol kembali

        // Mengambil data gambar yang dikirimkan dari AnalysisActivity
        val imageUri = intent.getStringExtra(AnalysisActivity.EXTRA_IMAGE_URI)
        val imagePath = intent.getStringExtra(AnalysisActivity.EXTRA_IMAGE_PATH)

        if (imageUri != null) {
            // Jika gambar dikirimkan dengan URI (dari galeri)
            val uri = Uri.parse(imageUri)
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imageView.setImageBitmap(bitmap)
        } else if (imagePath != null) {
            // Jika gambar dikirimkan dengan path file (dari kamera)
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                imageView.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "Gambar tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Gambar tidak ditemukan", Toast.LENGTH_SHORT).show()
        }

        // Mengambil hasil analisis dari intent
        val predictionType = intent.getStringExtra(EXTRA_PREDICTION_TYPE) ?: "Tidak diketahui"
        val confidenceScore = intent.getFloatExtra(EXTRA_CONFIDENCE_SCORE, 0f)

        // Menampilkan hasil analisis di TextViews
        resultTypeTextView.text = "Hasil: $predictionType"
        confidenceTextView.text = "Kepercayaan: ${(confidenceScore * 100).toInt()}%"

        // Tombol kembali
        backButton.setOnClickListener {
            onBackPressed() // Mengembalikan ke activity sebelumnya (AnalysisActivity)
        }
    }


    private fun correctImageOrientation(imageFile: File): Bitmap {
        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
        val exif = ExifInterface(FileInputStream(imageFile))

        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        var correctedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }

        // Jika berasal dari kamera depan, lakukan flip horizontal
        if (isFrontCamera(imageFile)) {
            correctedBitmap = flipBitmapHorizontally(correctedBitmap)
        }

        return correctedBitmap
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun flipBitmapHorizontally(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.preScale(-1f, 1f) // Pembalikan horizontal
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun isFrontCamera(imageFile: File): Boolean {
        // Logika sederhana untuk memeriksa apakah gambar berasal dari kamera depan.
        // Bisa diperluas dengan metadata atau nama file tertentu.
        val exif = ExifInterface(FileInputStream(imageFile))
        val lensFacing = exif.getAttribute(ExifInterface.TAG_MAKE) // Dummy untuk indikasi kamera depan
        return lensFacing?.contains("front", ignoreCase = true) == true
    }

    private fun resultImage() {
        // Ambil hasil analisis dari Intent
        val predictionType = intent.getStringExtra(EXTRA_PREDICTION_TYPE) ?: "Tidak Diketahui"
        val confidenceScore = intent.getFloatExtra(EXTRA_CONFIDENCE_SCORE, 0f)

        // Update TextView dengan hasil analisis
        resultTypeTextView.text = "Tipe Jerawat: $predictionType"
        confidenceTextView.text = "Confidence Score: %.2f".format(confidenceScore)
    }

    companion object {
        const val EXTRA_IMAGE_PATH = "extra_image_path"
        const val EXTRA_PREDICTION_TYPE = "extra_prediction_type"
        const val EXTRA_CONFIDENCE_SCORE = "extra_confidence_score"
    }
}