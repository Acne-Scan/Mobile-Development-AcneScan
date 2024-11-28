package com.dicoding.acnescan.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import java.io.File
import java.io.FileInputStream

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
                val correctedBitmap = correctImageOrientation(imageFile)
                binding.historyIcon.setImageBitmap(correctedBitmap) // Tampilkan gambar dengan orientasi benar
            } else {
                binding.historyIcon.setImageResource(R.drawable.pp_patrickk) // Placeholder jika file tidak ditemukan
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

        // Fungsi untuk memperbaiki orientasi gambar menggunakan ExifInterface
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
