package com.demo.moviesapp.screens.popularAndTopRatedList.viewModel.topRated

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.moviesapp.commonUtils.commonClasses.Response
import com.demo.moviesapp.screens.home.models.TopRatedMoviesResponse
import com.demo.moviesapp.screens.home.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TopRatedMoviesViewModel(private val repository: HomeRepository) : ViewModel() {

    init {
        getTopRatedMovies(1)
    }

    val topRatedMovies: LiveData<Response<TopRatedMoviesResponse>>
        get() = repository.topRatedMovies

    fun getTopRatedMovies(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTopRatedMovies(page)
        }
    }
}