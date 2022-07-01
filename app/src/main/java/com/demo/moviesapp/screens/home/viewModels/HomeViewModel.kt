package com.demo.moviesapp.screens.home.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.moviesapp.commonUtils.commonClasses.Response
import com.demo.moviesapp.screens.home.models.PopularMoviesResponse
import com.demo.moviesapp.screens.home.models.TopRatedMoviesResponse
import com.demo.moviesapp.screens.home.models.UpcomingMoviesResponse
import com.demo.moviesapp.screens.home.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    init {
        getUpComingMovies(1)
        getPopularMovies(1)
        getTopRatedMovies(1)
    }

    val upComingMovies: LiveData<Response<UpcomingMoviesResponse>>
        get() = repository.upComingMovies

    val popularMovies: LiveData<Response<PopularMoviesResponse>>
        get() = repository.popularMovies

    val topRatedMovies: LiveData<Response<TopRatedMoviesResponse>>
        get() = repository.topRatedMovies

    fun getUpComingMovies(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUpComingMovies(page)
        }
    }

    fun getPopularMovies(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPopularMovies(page)
        }
    }

    fun getTopRatedMovies(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTopRatedMovies(page)
        }
    }
}