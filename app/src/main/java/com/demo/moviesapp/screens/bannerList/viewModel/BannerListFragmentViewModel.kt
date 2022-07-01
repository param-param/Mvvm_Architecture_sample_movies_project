package com.demo.moviesapp.screens.bannerList.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.moviesapp.commonUtils.commonClasses.Response
import com.demo.moviesapp.screens.home.models.UpcomingMoviesResponse
import com.demo.moviesapp.screens.home.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BannerListFragmentViewModel(private val repository: HomeRepository) : ViewModel() {

    init {
        getUpComingMovies(1)
    }

    val upComingMovies: LiveData<Response<UpcomingMoviesResponse>>
        get() = repository.upComingMovies


    fun getUpComingMovies(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUpComingMovies(page)
        }
    }

}