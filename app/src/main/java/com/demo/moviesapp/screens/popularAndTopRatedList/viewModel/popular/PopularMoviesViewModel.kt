package com.demo.moviesapp.screens.popularAndTopRatedList.viewModel.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.moviesapp.commonUtils.commonClasses.Response
import com.demo.moviesapp.screens.home.models.PopularMoviesResponse
import com.demo.moviesapp.screens.home.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PopularMoviesViewModel(private val repository: HomeRepository) : ViewModel() {

    init {
        getPopularMovies(1)
    }

    val popularMovies: LiveData<Response<PopularMoviesResponse>>
        get() = repository.popularMovies


    fun getPopularMovies(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPopularMovies(page)
        }
    }
}