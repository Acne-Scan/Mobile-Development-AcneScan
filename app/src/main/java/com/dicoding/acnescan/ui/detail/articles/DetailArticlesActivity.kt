package com.dicoding.acnescan.ui.detail.articles

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.acnescan.databinding.ActivityDetailArticlesBinding

class DetailArticlesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticlesBinding
    private val detailArticlesViewModel by viewModels<DetailArticlesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up ViewBinding
        binding = ActivityDetailArticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val articleId = intent.getIntExtra(EXTRA_ARTICLE_ID, 0)
        val articleName = intent.getStringExtra(EXTRA_ARTICLE_NAME)
        val articleDescription = intent.getStringExtra(EXTRA_ARTICLE_DESCRIPTION)
        val articleImage = intent.getStringExtra(EXTRA_ARTICLE_IMAGE)
        val articleUpdatedAt = intent.getStringExtra(EXTRA_ARTICLE_UPDATE)

        // Menggunakan data dari Intent untuk mengisi tampilan
        binding.titleArticle.text = articleName
        binding.description.text = articleDescription
        Glide.with(this)
            .load(articleImage)
            .into(binding.imageView)

        // Observasi status loading atau error jika diperlukan
        observeViewModel()

        binding.buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun observeViewModel() {
        // Contoh jika ingin menampilkan status loading/error
        detailArticlesViewModel.loadingState.observe(this) { isLoading ->
            if (isLoading) {
                // Tampilkan loading
                binding.progressBar.visibility = View.VISIBLE
            } else {
                // Sembunyikan loading
                binding.progressBar.visibility = View.GONE
            }
        }

        detailArticlesViewModel.errorState.observe(this) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val EXTRA_ARTICLE_ID = "extra_article_id"
        const val EXTRA_ARTICLE_NAME = "extra_article_name"
        const val EXTRA_ARTICLE_DESCRIPTION = "extra_article_description"
        const val EXTRA_ARTICLE_IMAGE = "extra_article_image"
        const val EXTRA_ARTICLE_UPDATE = "extra_article_update"
    }
}
