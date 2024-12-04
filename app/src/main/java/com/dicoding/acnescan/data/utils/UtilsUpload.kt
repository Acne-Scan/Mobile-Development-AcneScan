package com.dicoding.acnescan.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val MAXIMAL_SIZE = 1024 * 1024 // 1 MB
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

// Fungsi untuk mengonversi Uri menjadi File
fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = File.createTempFile(timeStamp, ".jpg", context.cacheDir)
    val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream?.read(buffer).also { length = it ?: -1 }!! > 0) {
        outputStream.write(buffer, 0, length)
    }
    inputStream?.close()
    outputStream.close()
    return myFile
}

// Fungsi untuk mereduksi ukuran gambar
fun File.reduceFileImage(): File {
    val bitmap = BitmapFactory.decodeFile(this.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(this))
    return this
}

// Fungsi untuk membuat file sementara untuk menyimpan gambar yang akan dianalisis
fun createCustomTempFile(context: Context): File {
    // Membuat timestamp untuk nama file
    val timeStamp = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())
    // Membuat file sementara di direktori cache
    return File.createTempFile("temp_image_$timeStamp", ".jpg", context.cacheDir)
}


// Fungsi mengubah gambar menjadi bentuk android/json
fun convertImageToBase64(imageFile: File): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    try {
        val fileInputStream = FileInputStream(imageFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (fileInputStream.read(buffer).also { length = it } != -1) {
            byteArrayOutputStream.write(buffer, 0, length)
        }
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    } catch (e: IOException) {
        e.printStackTrace()
        return ""
    }
}

// Extension function untuk mengonversi String menjadi Float?
fun Float?.toSafeFloat(): Float? {
    return try {
        this // Coba konversi ke Float
    } catch (e: NumberFormatException) {
        null // Jika gagal, kembalikan null
    }
}