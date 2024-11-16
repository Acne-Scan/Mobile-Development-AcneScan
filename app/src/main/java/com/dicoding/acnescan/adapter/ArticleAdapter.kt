package com.dicoding.acnescan.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.acnescan.R
import com.dicoding.acnescan.model.ArticleItem

class ArticleAdapter(private var articles: List<ArticleItem>) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.article_image)
        val titleView: TextView = itemView.findViewById(R.id.article_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_articles, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val item = articles[position]
        Glide.with(holder.itemView.context)
            .load(item.imageRes) // Bisa gunakan URL jika gambar dari server
            .into(holder.imageView)
        holder.titleView.text = item.title
    }

    override fun getItemCount(): Int = articles.size

    // Tambahkan fungsi untuk memperbarui data jika dibutuhkan
    fun updateData(newArticles: List<ArticleItem>) {
        articles = newArticles
        notifyDataSetChanged()
    }
}
