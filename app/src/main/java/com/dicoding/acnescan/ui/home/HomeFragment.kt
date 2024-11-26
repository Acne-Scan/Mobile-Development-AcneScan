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
import com.dicoding.acnescan.ui.adapters.ArticleAdapter

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

        binding.articleCarousel.adapter = _articleAdapter
    }

    fun getDataArticles() {
        homeViewModel.articles.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    // Handle loading state
                }
                is ResultState.Success -> {
                    // Handle
//                    _articleAdapter.submitList(result.data)
                    Log.d("TAG", "getDataArticles: ${result.data}")
                }
                is ResultState.Error -> {
                    // Handle error state
                }
            }
        }
    }
}