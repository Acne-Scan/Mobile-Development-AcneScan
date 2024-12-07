package com.dicoding.acnescan.ui.products

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        getDataProducts()
    }

    private fun getDataProducts() {
        homeViewModel.getProducts()
        homeViewModel.products.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.productList.visibility = View.GONE // Sembunyikan RecyclerView saat loading
                    binding.emptyState.visibility = View.GONE // Sembunyikan empty state
                }
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (result.data.isEmpty()) {
                        binding.productList.visibility = View.GONE
                        binding.emptyState.visibility = View.VISIBLE // Tampilkan empty state jika data kosong
                    } else {
                        binding.productList.visibility = View.VISIBLE
                        binding.emptyState.visibility = View.GONE
                        _productAdapter.submitList(result.data) // Update data ke adapter
                    }
                    Log.d("ProductsFragment", "getDataProducts: ${result.data}")
                }
                is ResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.productList.visibility = View.GONE
                    binding.emptyState.visibility = View.VISIBLE // Tampilkan empty state saat error
                    Log.e("ProductsFragment", "Error loading products: ${result.message}")
                }
            }
        }
    }

//    override fun onResume(){
//        super.onResume()
//        // Menyegarkan data setiap kali fragment dikembalikan ke layar
//        getDataProducts()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}