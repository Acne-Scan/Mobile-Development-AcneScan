package com.dicoding.acnescan.data.adapter

import android.graphics.Bitmap
import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.exifinterface.media.ExifInterface
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.acnescan.R
import com.dicoding.acnescan.databinding.ItemHistoryBinding
import com.dicoding.acnescan.database.HistoryEntity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.request.RequestOptions
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

class HistoryAdapter(private val onItemClick: (HistoryEntity) -> Unit) :
    ListAdapter<HistoryEntity, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val getHistory = getItem(position)
        holder.bind(getHistory, onItemClick)
    }

    class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryEntity, onItemClick: (HistoryEntity) -> Unit) {
            // Konversi timestamp ke format menit
            val formattedTime = getRelativeTime(history.timestamp)
            binding.historyTime.text = formattedTime

            // Koreksi orientasi gambar jika file gambar ada
            val imageFile = File(history.imagePath)
            if (imageFile.exists()) {
                // Gunakan Glide untuk memuat gambar dan koreksi orientasi di background thread
                Glide.with(binding.historyIcon.context)
                    .load(imageFile)
                    .apply(RequestOptions().transform(GlideRotationAndFlipTransformation(imageFile))) // Apply rotation and flip transformation
                    .into(binding.historyIcon)
            } else {
                binding.historyIcon.setImageResource(R.drawable.ic_notifications_black_24dp) // Placeholder jika file tidak ditemukan
            }

            binding.root.setOnClickListener {
                onItemClick(history)
            }
        }

        //Fungsi untuk menghitung waktu relatif dari timestamp.
        private fun getRelativeTime(timestamp: String): String {
            return try {
                // Konversi string timestamp ke format long
                val timeMillis = timestamp.toLong()
                val now = System.currentTimeMillis()

                val difference = now - timeMillis
                val minutes = difference / (1000 * 60)

                when {
                    minutes < 1 -> "Baru saja"
                    minutes < 60 -> "$minutes menit yang lalu"
                    minutes < 1440 -> "${minutes / 60} jam yang lalu"
                    else -> "${minutes / 1440} hari yang lalu"
                }
            } catch (e: Exception) {
                "Waktu tidak valid"
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryEntity>() {
            override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}

// Glide Transformation untuk rotasi dan pembalikan gambar
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