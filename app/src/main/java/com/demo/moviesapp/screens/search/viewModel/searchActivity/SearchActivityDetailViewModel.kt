package com.demo.moviesapp.screens.search.viewModel.searchActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.moviesapp.commonUtils.commonClasses.Response
import com.demo.moviesapp.screens.movieDetail.models.MovieDetailResponse
import com.demo.moviesapp.screens.search.repository.SearchActivityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SearchActivityDetailViewModel(private val repository: SearchActivityRepository) :
    ViewModel() {

    val searchMovieDetail: LiveData<Response<MovieDetailResponse>>
        get() = repository.searchMovieDetail


    fun getSearchedMovies(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSearchMovieDetail(movieId)
        }
    }

}