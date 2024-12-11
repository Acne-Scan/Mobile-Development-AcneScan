package com.dicoding.acnescan.ui.products

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.acnescan.data.factory.ViewModelFactory
import com.dicoding.acnescan.data.utils.ResultState
import com.dicoding.acnescan.databinding.FragmentProductsBinding

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private lateinit var _productAdapter: ProductListAdapter

    private val homeViewModel by viewModels<ProductsViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set LayoutManager untuk RecyclerView
        binding.productList.layoutManager = LinearLayoutManager(requireContext())

        // Inisialisasi adapter
        _productAdapter = ProductListAdapter()

        // Set adapter ke RecyclerView
        binding.productList.adapter = _productAdapter

        // Observasi data dari ViewModel
        observeViewModel()

        // Setup pencarian
        setupSearch()
    }

    private fun observeViewModel() {
        // Ambil semua produk
        homeViewModel.getProducts()

        // Observasi hasil pencarian (data yang telah difilter)
        homeViewModel.filteredProducts.observe(viewLifecycleOwner) { filteredList ->
            if (filteredList.isEmpty()) {
                binding.productList.visibility = View.GONE
                binding.emptyState.visibility = View.VISIBLE
            } else {
                binding.productList.visibility = View.VISIBLE
                binding.emptyState.visibility = View.GONE
                _productAdapter.submitList(filteredList)
            }
        }

        // Observasi status (loading/success/error)
        homeViewModel.products.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.productList.visibility = View.GONE
                    binding.emptyState.visibility = View.GONE
                }
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                }
                is ResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.productList.visibility = View.GONE
                    binding.emptyState.visibility = View.VISIBLE
                    Log.e("ProductsFragment", "Error loading products: ${result.message}")
                }
            }
        }
    }

    private fun setupSearch() {
        // Mengatur logika pencarian pada SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { homeViewModel.search(it) } // Kirim query ke ViewModel
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { homeViewModel.search(it) } // Kirim query ke ViewModel
                return true
            }
        })
    }

//    override fun onResume() {
//        super.onResume()
//        // Menyegarkan data setiap kali fragment dikembalikan ke layar
//        observeViewModel()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}