package com.dicoding.acnescan.ui.detail.articles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailArticlesViewModel : ViewModel() {

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> get() = _errorState

    fun setLoading(isLoading: Boolean) {
        _loadingState.value = isLoading
    }

    fun setError(message: String) {
        _errorState.value = message
    }
}
