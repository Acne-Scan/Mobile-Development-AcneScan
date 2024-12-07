package com.dicoding.acnescan.data.adapter.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.acnescan.databinding.ItemArticlesBinding
import com.dicoding.acnescan.data.model.response.DataItemArticles
import com.dicoding.acnescan.ui.detail.articles.DetailArticlesActivity

class ArticleAdapter(private val onItemClicked: (DataItemArticles) -> Unit): RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    private var articleList: List<DataItemArticles> = emptyList()

    class ArticleViewHolder(private val binding: ItemArticlesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(articlesData: DataItemArticles, onItemClick: (DataItemArticles) -> Unit) {
            binding.articleTitle.text = articlesData.name

            Glide.with(binding.root.context)
                .load(articlesData.image)
                .into(binding.articleImage)

            binding.root.setOnClickListener {
                // Memanggil fungsi klik item (untuk kemungkinan penggunaan di Fragment/Activity lain)
                onItemClick(articlesData)

                // Intent untuk pindah ke DetailArticlesActivity
                val context = binding.root.context
                val intent = Intent(context, DetailArticlesActivity::class.java).apply {
                    // Menambahkan data ke dalam Intent
                    putExtra(DetailArticlesActivity.EXTRA_ARTICLE_ID, articlesData.articleId)
                    putExtra(DetailArticlesActivity.EXTRA_ARTICLE_NAME, articlesData.name)
                    putExtra(DetailArticlesActivity.EXTRA_ARTICLE_DESCRIPTION, articlesData.description)
                    putExtra(DetailArticlesActivity.EXTRA_ARTICLE_IMAGE, articlesData.image)
                    putExtra(DetailArticlesActivity.EXTRA_ARTICLE_UPDATE, articlesData.updatedAt)
                }

                // Menyalakan Activity baru
                context.startActivity(intent)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val item = articleList[position]
        holder.bind(item, onItemClicked)
    }

    override fun getItemCount(): Int = articleList.size

    fun submitList(articles: List<DataItemArticles>) {
        articleList = articles
        notifyDataSetChanged()
    }
}