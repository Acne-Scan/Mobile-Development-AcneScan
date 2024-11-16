package com.dicoding.acnescan.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.acnescan.R
import com.dicoding.acnescan.ui.adapters.ArticleAdapter
import com.dicoding.acnescan.ui.adapters.RecommendationAdapter
import com.dicoding.acnescan.ui.decorators.CarouselItemDecoration

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observasi Greeting Text
        val greeting = view.findViewById<TextView>(R.id.greeting)
        homeViewModel.greeting.observe(viewLifecycleOwner) { text ->
            greeting.text = text
        }

        // Konfigurasi ViewPager2 untuk Rekomendasi
        val recommendationCarousel = view.findViewById<ViewPager2>(R.id.recommendation_carousel)
        homeViewModel.recommendations.observe(viewLifecycleOwner) { recommendations ->
            recommendationCarousel.adapter = RecommendationAdapter(recommendations)
            recommendationCarousel.clipToPadding = false
            recommendationCarousel.clipChildren = false
            recommendationCarousel.offscreenPageLimit = 3
            recommendationCarousel.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            recommendationCarousel.addItemDecoration(CarouselItemDecoration(32)) // Tambahkan margin

//            recommendationCarousel.setPageTransformer { page, position ->
//                val scale = 0.85f + (1 - Math.abs(position)) * 0.15f
//                page.scaleY = scale
//                page.alpha = 0.5f + (1 - Math.abs(position)) * 0.5f
//            }
        }


        // Konfigurasi ViewPager2 untuk Artikel
        val articleCarousel = view.findViewById<ViewPager2>(R.id.article_carousel)
        homeViewModel.articles.observe(viewLifecycleOwner) { articles ->
            articleCarousel.adapter = ArticleAdapter(articles)
            articleCarousel.clipToPadding = false
            articleCarousel.clipChildren = false
            articleCarousel.offscreenPageLimit = 3
            articleCarousel.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            articleCarousel.addItemDecoration(CarouselItemDecoration(32))

//            articleCarousel.setPageTransformer { page, position ->
//                val scale = 0.85f + (1 - Math.abs(position)) * 0.15f
//                page.scaleY = scale
//                page.alpha = 0.5f + (1 - Math.abs(position)) * 0.5f
//            }
        }
    }

    // Fungsi untuk menambahkan efek carousel
    private fun applyCarouselEffect(viewPager: ViewPager2) {
        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.offscreenPageLimit = 3
        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        // Tambahkan dekorasi untuk margin antar item
        viewPager.addItemDecoration(CarouselItemDecoration(margin = 32))

        // Tambahkan efek carousel
        viewPager.setPageTransformer { page, position ->
            val scale = 0.85f + (1 - Math.abs(position)) * 0.15f
            page.scaleY = scale
            page.scaleX = scale
            page.alpha = 0.5f + (1 - Math.abs(position)) * 0.5f
        }
    }
}