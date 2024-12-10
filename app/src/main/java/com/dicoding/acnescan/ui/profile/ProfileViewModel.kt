package com.dicoding.acnescan.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.acnescan.data.factory.Repository

class ProfileViewModel(private val repository: Repository) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    fun fetchUsername(context: Context) {
        _username.value = repository.getUsername(context)
    }
}