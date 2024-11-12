package com.dicoding.acnescan.ui.rekomendasi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RekomendasiViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Rekomendasi Fragment"
    }
    val text: LiveData<String> = _text
}