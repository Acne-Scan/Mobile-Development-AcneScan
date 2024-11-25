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
import java.io.File
import java.io.FileInputStream

class AnalysisActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView

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
                imageView.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show()
            }
        } else if (imageUri != null) {
            // Gambar dari galeri
            val uri = Uri.parse(imageUri)
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imageView.setImageBitmap(bitmap)
        } else {
            Toast.makeText(this, "No image provided", Toast.LENGTH_SHORT).show()
        }

        // Tombol kembali
        findViewById<Button>(R.id.button_back).setOnClickListener {
            finish()
        }

        // Tombol analyze
        findViewById<Button>(R.id.button_analyze).setOnClickListener {
            analyzeImage(imagePath ?: imageUri)
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

    private fun analyzeImage(imagePath: String?) {
        if (imagePath == null) {
            Toast.makeText(this, "No image to analyze", Toast.LENGTH_SHORT).show()
            return
        }

        // Placeholder untuk analisis gambar
        Toast.makeText(this, "Analyzing image...", Toast.LENGTH_SHORT).show()

        // Pindah ke activity hasil analisis (ResultActivity)
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_PATH, imagePath)
        startActivity(intent)
    }

    companion object {
        const val EXTRA_IMAGE_PATH = "extra_image_path"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}