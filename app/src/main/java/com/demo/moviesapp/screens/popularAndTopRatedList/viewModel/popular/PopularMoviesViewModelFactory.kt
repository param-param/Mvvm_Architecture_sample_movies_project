package com.demo.moviesapp.screens.popularAndTopRatedList.viewModel.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.moviesapp.screens.home.repository.HomeRepository
import javax.inject.Inject


class PopularMoviesViewModelFactory @Inject constructor(private val repository: HomeRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PopularMoviesViewModel(repository) as T
    }

}