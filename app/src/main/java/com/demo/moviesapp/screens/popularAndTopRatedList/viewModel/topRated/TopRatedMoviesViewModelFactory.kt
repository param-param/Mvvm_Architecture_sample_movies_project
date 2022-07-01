package com.demo.moviesapp.screens.popularAndTopRatedList.viewModel.topRated

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.moviesapp.screens.home.repository.HomeRepository
import javax.inject.Inject


class TopRatedMoviesViewModelFactory @Inject constructor(private val repository: HomeRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TopRatedMoviesViewModel(repository) as T
    }

}