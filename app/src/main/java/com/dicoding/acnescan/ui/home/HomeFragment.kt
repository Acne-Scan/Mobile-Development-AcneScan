package com.dicoding.acnescan.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.acnescan.databinding.FragmentHomeBinding
import com.dicoding.acnescan.factory.ViewModelFactory
import com.dicoding.acnescan.response.ResultState
import com.dicoding.acnescan.adapter.ArticleAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var _articleAdapter: ArticleAdapter // Adapter untuk Article

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi adapter dengan klik handler
        _articleAdapter = ArticleAdapter { dataItem ->
            // Handle klik item
            Log.d("ArticleAdapter", "Clicked item: ${dataItem.name}")
        }

        // Set adapter ke RecyclerView
        binding.articleCarousel.adapter = _articleAdapter

        // Observasi data dari ViewModel
        getDataArticles()
    }

    private fun getDataArticles() {
        homeViewModel.articles.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.articleCarousel.visibility = View.GONE // Sembunyikan carousel saat loading
                    binding.emptyState.visibility = View.GONE // Sembunyikan empty state
                }
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (result.data.isNullOrEmpty()) {
                        // Jika data kosong, tampilkan empty state
                        binding.articleCarousel.visibility = View.GONE
                        binding.emptyState.visibility = View.VISIBLE
                    } else {
                        // Jika data ada, tampilkan carousel
                        binding.articleCarousel.visibility = View.VISIBLE
                        binding.emptyState.visibility = View.GONE
//                        _articleAdapter.submitList(result.data)
                    }
                    Log.d("TAG", "getDataArticles: ${result.data}")
                }
                is ResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.articleCarousel.visibility = View.GONE
                    binding.emptyState.visibility = View.VISIBLE // Tampilkan empty state saat error
                    // Anda juga bisa menampilkan pesan error kepada pengguna
                    Log.e("TAG", "Error loading articles: ${result.message}")
                }
            }
        }
    }



}