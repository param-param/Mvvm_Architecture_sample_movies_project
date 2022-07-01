package com.demo.moviesapp.screens.movieDetail.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.moviesapp.commonUtils.commonClasses.Response
import com.demo.moviesapp.screens.home.models.PopularMoviesResponse
import com.demo.moviesapp.screens.home.models.TopRatedMoviesResponse
import com.demo.moviesapp.screens.home.models.UpcomingMoviesResponse
import com.demo.moviesapp.screens.movieDetail.models.MovieDetailResponse
import com.demo.moviesapp.screens.movieDetail.repository.MovieDetailActivityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailActivityViewModel(private val repository: MovieDetailActivityRepository) :
    ViewModel() {


    val upComingMovies: LiveData<Response<UpcomingMoviesResponse>>
        get() = repository.upComingMovies

    val popularMovies: LiveData<Response<PopularMoviesResponse>>
        get() = repository.popularMovies

    val topRatedMovies: LiveData<Response<TopRatedMoviesResponse>>
        get() = repository.topRatedMovies

    val movieDetail: LiveData<Response<MovieDetailResponse>>
        get() = repository.movieDetail

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

    fun getMovieDetail(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMovieDetail(movieId)
        }
    }
}