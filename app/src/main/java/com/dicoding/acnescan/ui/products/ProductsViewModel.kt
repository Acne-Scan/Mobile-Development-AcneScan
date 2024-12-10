package com.dicoding.acnescan.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.acnescan.data.factory.Repository
import com.dicoding.acnescan.data.model.response.DataItemProducts
import com.dicoding.acnescan.data.utils.ResultState
import kotlinx.coroutines.launch

class ProductsViewModel(private val repository: Repository) : ViewModel() {

    private val _products = MutableLiveData<ResultState<List<DataItemProducts>>>()
    val products: LiveData<ResultState<List<DataItemProducts>>> get() = _products

    private val _filteredProducts = MutableLiveData<List<DataItemProducts>>()
    val filteredProducts: LiveData<List<DataItemProducts>> get() = _filteredProducts

    private var originalProducts: List<DataItemProducts> = emptyList() // Data asli untuk referensi pencarian

    fun getProducts() {
        viewModelScope.launch {
            repository.getProducts().observeForever { result ->
                _products.value = result
                if (result is ResultState.Success) {
                    originalProducts = result.data // Simpan data asli
                    _filteredProducts.value = result.data // Awalnya, tampilkan semua produk
                }
            }
        }
    }

    fun search(query: String) {
        // Filter produk berdasarkan nama produk atau deskripsi
        _filteredProducts.value = if (query.isBlank()) {
            originalProducts // Jika query kosong, kembalikan daftar asli
        } else {
            originalProducts.filter { product ->
                product.name.contains(query, ignoreCase = true) || // Filter berdasarkan nama produk
                        product.description.contains(query, ignoreCase = true) // Filter berdasarkan deskripsi produk
            }
        }
    }
}