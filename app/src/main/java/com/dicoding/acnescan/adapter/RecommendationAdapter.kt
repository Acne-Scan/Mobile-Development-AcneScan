package com.dicoding.acnescan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.acnescan.R
import com.dicoding.acnescan.model.RecommendationItem

class RecommendationAdapter(private var items: List<RecommendationItem>) :
    RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    inner class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.recommendation_image)
        val titleView: TextView = itemView.findViewById(R.id.recommendation_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommendation, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView.context)
            .load(item.imageRes) // Bisa gunakan URL jika gambar dari server
            .into(holder.imageView)
        holder.titleView.text = item.title
    }

    override fun getItemCount(): Int = items.size

    // Fungsi untuk memperbarui data jika dibutuhkan
    fun updateDataRecommendation(newRecommendations: List<RecommendationItem>) {
        items = newRecommendations
        notifyDataSetChanged()
    }
}