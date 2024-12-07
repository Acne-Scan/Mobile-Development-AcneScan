package com.dicoding.acnescan.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.acnescan.data.factory.Repository
import com.dicoding.acnescan.data.model.response.DataItemProducts
import com.dicoding.acnescan.data.utils.ResultState

class ProductsViewModel (private val repository: Repository) : ViewModel() {

    private val _products = MutableLiveData<ResultState<List<DataItemProducts>>>()

    val products: LiveData<ResultState<List<DataItemProducts>>> get() = _products

    fun getProducts() {
        repository.getProducts().observeForever { result ->
            _products.value = result
        }
    }
}