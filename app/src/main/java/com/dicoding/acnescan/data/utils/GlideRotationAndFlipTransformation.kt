package com.dicoding.acnescan.data.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

class GlideRotationAndFlipTransformation(private val imageFile: File) : BitmapTransformation() {

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val exif = ExifInterface(FileInputStream(imageFile))
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

        val rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(toTransform, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(toTransform, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(toTransform, 270f)
            ExifInterface.ORIENTATION_NORMAL -> toTransform
            else -> toTransform
        }

        return rotatedBitmap
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        // Add a unique key to Glide's disk cache
        messageDigest.update("rotate_flip".toByteArray())
    }
}