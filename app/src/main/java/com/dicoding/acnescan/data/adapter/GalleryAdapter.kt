package com.dicoding.acnescan.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.acnescan.R
import com.dicoding.acnescan.databinding.ItemHistoryBinding
import com.dicoding.acnescan.database.GalleryEntity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.acnescan.data.utils.GlideRotationAndFlipTransformation
import java.io.File

class GalleryAdapter(
    private val onItemClick: (GalleryEntity) -> Unit,
    private val onItemLongPress: (GalleryEntity) -> Unit
) : ListAdapter<GalleryEntity, GalleryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history, onItemClick, onItemLongPress)
    }

    class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(history: GalleryEntity, onItemClick: (GalleryEntity) -> Unit, onItemLongPress: (GalleryEntity) -> Unit) {
            // Konversi timestamp ke format relatif
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
                binding.historyIcon.setImageResource(R.drawable.ic_notification)
            }

            // Klik biasa untuk melihat gambar lebih lanjut
            binding.root.setOnClickListener {
                onItemClick(history)
            }

            // Long click untuk menghapus history
            binding.root.setOnLongClickListener {
                onItemLongPress(history)
                true // Return harus true jika menggunakan long click listener
            }
        }

        // Fungsi untuk menghitung waktu relatif dari timestamp.
        private fun getRelativeTime(timestamp: String): String {
            return try {
                val timeMillis = timestamp.toLong()
                val now = System.currentTimeMillis()

                val difference = now - timeMillis
                val minutes = difference / (1000 * 60)

                when {
                    minutes < 1 -> "Now"
                    minutes < 60 -> "$minutes minutes ago"
                    minutes < 1440 -> "${minutes / 60} hours ago"
                    else -> "${minutes / 1440} days ago"
                }
            } catch (e: Exception) {
                "Times not valid"
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GalleryEntity>() {
            override fun areItemsTheSame(oldItem: GalleryEntity, newItem: GalleryEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GalleryEntity, newItem: GalleryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}