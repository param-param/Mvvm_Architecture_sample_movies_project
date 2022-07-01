package com.demo.moviesapp.screens.movieDetail.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.moviesapp.screens.movieDetail.repository.MovieDetailActivityRepository
import javax.inject.Inject

class MovieDetailActivityViewModelFactory @Inject constructor(private val repository: MovieDetailActivityRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieDetailActivityViewModel(repository) as T
    }

}
