package com.dicoding.acnescan.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.acnescan.databinding.FragmentHomeBinding
import com.dicoding.acnescan.data.factory.ViewModelFactory
import com.dicoding.acnescan.data.utils.ResultState
import com.dicoding.acnescan.data.adapter.home.ArticleAdapter
import com.dicoding.acnescan.data.adapter.home.ProductAdapter
import com.dicoding.acnescan.ui.BottomNavigation
import com.dicoding.acnescan.util.SharedPrefUtil

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var _articleAdapter: ArticleAdapter // Adapter untuk Article
    private lateinit var _productAdapter: ProductAdapter // Adapter untuk Product

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

        // Cek status login dan tampilkan username jika login berhasil
        val token = SharedPrefUtil.getToken(requireContext())
        if (token != null) {
            // Jika ada token, berarti pengguna sudah login, tampilkan username
            val username = SharedPrefUtil.getUsername(requireContext())
            binding.tvUsername.text = username
            binding.tvUsername.visibility = View.VISIBLE
        } else {
            // Jika token tidak ada, sembunyikan tv_name
            binding.tvUsername.visibility = View.GONE
        }
        
        // Inisialisasi adapter dengan klik handler
        _articleAdapter = ArticleAdapter { dataItem ->
            // Handle klik item
            Log.d("ArticleAdapter", "Clicked item: ${dataItem.name}")
        }
        _productAdapter = ProductAdapter { dataItem ->
            // Handle klik item
            Log.d("ProductAdapter", "Clicked item: ${dataItem.image}")
        }

        // Set adapter ke RecyclerView
        binding.articleCarousel.adapter = _articleAdapter
        binding.productCarousel.adapter = _productAdapter

        // Observasi data dari ViewModel
        getDataArticles()
        getDataProducts()
    }

    private fun getDataArticles() {
        homeViewModel.getArticles()
        homeViewModel.articles.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.articleTitle.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    binding.productCarousel.visibility = View.GONE // Sembunyikan carousel saat loading
                    binding.emptyState.visibility = View.GONE // Sembunyikan empty state
                }
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (result.data.isEmpty()) {
                        // Jika data kosong, tampilkan empty state
                        binding.articleTitle.visibility = View.GONE
                        binding.productCarousel.visibility = View.GONE
                        binding.emptyState.visibility = View.VISIBLE
                    } else {
                        // Jika data ada, tampilkan carousel
                        binding.articleTitle.visibility = View.VISIBLE
                        binding.productCarousel.visibility = View.VISIBLE
                        binding.emptyState.visibility = View.GONE
                        _articleAdapter.submitList(result.data)
                    }
                    Log.d("HomeFragment", "getDataArticles: ${result.data}")
                }
                is ResultState.Error -> {
                    binding.articleTitle.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.productCarousel.visibility = View.GONE
                    binding.emptyState.visibility = View.VISIBLE// Tampilkan empty state saat error
                    // Anda juga bisa menampilkan pesan error kepada pengguna
                    Log.e("HomeFragment", "Error loading products: ${result.message}")

                    onPause()
                }
            }
        }
    }

    private fun getDataProducts() {
        homeViewModel.getProducts()
        homeViewModel.products.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.productTitle.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    binding.productCarousel.visibility = View.GONE // Sembunyikan carousel saat loading
                    binding.emptyState.visibility = View.GONE // Sembunyikan empty state
                }
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (result.data.isEmpty()) {
                        // Jika data kosong, tampilkan empty state
                        binding.productTitle.visibility = View.GONE
                        binding.productCarousel.visibility = View.GONE
                        binding.emptyState.visibility = View.VISIBLE
                    } else {
                        // Jika data ada, tampilkan carousel
                        binding.productTitle.visibility = View.VISIBLE
                        binding.productCarousel.visibility = View.VISIBLE
                        binding.emptyState.visibility = View.GONE
                        _productAdapter.submitList(result.data)
                    }
                    Log.d("HomeFragment", "getDataProducts: ${result.data}")
                }
                is ResultState.Error -> {
                    binding.productTitle.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.productCarousel.visibility = View.GONE
                    binding.emptyState.visibility = View.VISIBLE// Tampilkan empty state saat error
                    // Anda juga bisa menampilkan pesan error kepada pengguna
                    Log.e("HomeFragment", "Error loading products: ${result.message}")

                    onPause()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Menyegarkan data setiap kali fragment dikembalikan ke layar
//        getDataArticles()
//        getDataProducts()

        // Menonaktifkan swipe untuk ViewPager2 utama di activity/fragment induk
        (activity as? BottomNavigation)?.binding?.navHostFragmentActivityBottomNav?.isUserInputEnabled = false
    }

    override fun onPause() {
        super.onPause()
        // Mengaktifkan kembali swipe untuk ViewPager2 utama
        (activity as? BottomNavigation)?.binding?.navHostFragmentActivityBottomNav?.isUserInputEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}