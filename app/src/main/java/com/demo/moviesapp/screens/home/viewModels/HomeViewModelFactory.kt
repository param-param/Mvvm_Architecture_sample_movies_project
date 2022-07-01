package com.demo.moviesapp.screens.home.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.moviesapp.screens.home.repository.HomeRepository
import javax.inject.Inject

class HomeViewModelFactory @Inject constructor(private val repository: HomeRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }

}