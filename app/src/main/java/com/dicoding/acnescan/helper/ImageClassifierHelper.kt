package com.dicoding.acnescan.helper

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class ImageClassifierHelper(private val context: Context) {

    private var interpreter: Interpreter? = null

    init {
        try {
            // Memuat model TFLite dari assets
            val assetManager = context.assets
            val modelPath = "best_model2.tflite" // Ganti dengan nama model Anda
            val fileDescriptor = assetManager.openFd(modelPath)
            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength
            val mappedByteBuffer =
                fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)

            // Membuat Interpreter untuk model
            interpreter = Interpreter(mappedByteBuffer)
        } catch (e: Exception) {
            Log.e("ImageClassifierHelper", "Error loading model: ${e.message}")
        }
    }

    /**
     * Preprocessing gambar untuk memenuhi format input model TFLite
     */
    private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        val width = 150 // Dimensi input model
        val height = 150
        val byteBuffer = ByteBuffer.allocateDirect(4 * width * height * 3) // 4 byte untuk Float
        byteBuffer.order(ByteOrder.nativeOrder())

        // Resize bitmap ke ukuran model
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)

        // Normalisasi nilai pixel dan salin ke ByteBuffer
        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = resizedBitmap.getPixel(x, y)
                byteBuffer.putFloat(((pixel shr 16) and 0xFF) / 255.0f) // Red
                byteBuffer.putFloat(((pixel shr 8) and 0xFF) / 255.0f)  // Green
                byteBuffer.putFloat((pixel and 0xFF) / 255.0f)          // Blue
            }
        }
        return byteBuffer
    }

    /**
     * Fungsi untuk menjalankan inferensi gambar
     * @param bitmap Bitmap gambar input
     * @return Pair<String, Float> - Hasil klasifikasi dan tingkat kepercayaan
     */
    fun classifyImage(bitmap: Bitmap, outputBuffer: Array<FloatArray>): Pair<String, Float> {
        // Preprocess gambar sebelum inferensi
        val input = preprocessImage(bitmap)

        // Output array (disesuaikan untuk model dengan 5 kelas)
        val output = Array(1) { FloatArray(5) } // Output shape [1, 5]

        // Jalankan inferensi
        interpreter?.run(input, output)

        // Ambil hasil prediksi (kelas dengan probabilitas tertinggi)
        val probabilities = output[0] // FloatArray(5), hasil probabilitas untuk setiap kelas
        val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
        val confidenceScore = probabilities[maxIndex]

        // Nama kelas berdasarkan indeks
        val classNames = arrayOf("Blackheads", "Cyst", "Papules", "Pustules", "Whiteheads") // Sesuaikan nama kelas Anda
        val predictionType = if (maxIndex in classNames.indices) classNames[maxIndex] else "Unknown"

        return Pair(predictionType, confidenceScore)
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }
}