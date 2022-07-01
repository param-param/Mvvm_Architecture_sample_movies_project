package com.demo.moviesapp.screens.home.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.moviesapp.commonUtils.commonClasses.Response
import com.demo.moviesapp.api.ApiService
import com.demo.moviesapp.commonUtils.Constants
import com.demo.moviesapp.commonUtils.Utils
import com.demo.moviesapp.screens.home.models.PopularMoviesResponse
import com.demo.moviesapp.screens.home.models.TopRatedMoviesResponse
import com.demo.moviesapp.screens.home.models.UpcomingMoviesResponse
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val apiService: ApiService,
    private val applicationContext: Context
) {

    private val upComingMoviesResponseLiveData = MutableLiveData<Response<UpcomingMoviesResponse>>()
    val upComingMovies: LiveData<Response<UpcomingMoviesResponse>>
        get() = upComingMoviesResponseLiveData

    private val popularMoviesResponseLiveData = MutableLiveData<Response<PopularMoviesResponse>>()
    val popularMovies: LiveData<Response<PopularMoviesResponse>>
        get() = popularMoviesResponseLiveData

    private val topRatedMoviesResponseLiveData = MutableLiveData<Response<TopRatedMoviesResponse>>()
    val topRatedMovies: LiveData<Response<TopRatedMoviesResponse>>
        get() = topRatedMoviesResponseLiveData

    suspend fun getUpComingMovies(page: Int) {

        if (Utils.isInternetAvailable(applicationContext)) {
            try {
                val result = apiService.getUpcomingMoviesList(Constants.API_KEY, page)
                if (result?.body() != null) {
                    upComingMoviesResponseLiveData.postValue(Response.Success(result.body()))
                }
            } catch (exception: Exception) {
                upComingMoviesResponseLiveData.postValue(Response.Error(exception.message.toString()))
            }
        } else {
            upComingMoviesResponseLiveData.postValue(Response.Error(Constants.NO_INTERNET_CONNECTION))
        }
    }

    suspend fun getPopularMovies(page: Int) {

        if (Utils.isInternetAvailable(applicationContext)) {
            try {
                val result = apiService.getPopularMoviesList(Constants.API_KEY, page)
                if (result?.body() != null) {
                    popularMoviesResponseLiveData.postValue(Response.Success(result.body()))
                }
            } catch (exception: Exception) {
                popularMoviesResponseLiveData.postValue(Response.Error(exception.message.toString()))
            }
        } else {
            popularMoviesResponseLiveData.postValue(Response.Error(Constants.NO_INTERNET_CONNECTION))
        }
    }

    suspend fun getTopRatedMovies(page: Int) {

        if (Utils.isInternetAvailable(applicationContext)) {
            try {
                val result = apiService.getTopRatedMoviesList(Constants.API_KEY, page)
                if (result?.body() != null) {
                    topRatedMoviesResponseLiveData.postValue(Response.Success(result.body()))
                }
            } catch (exception: Exception) {
                topRatedMoviesResponseLiveData.postValue(Response.Error(exception.message.toString()))
            }
        } else {
            topRatedMoviesResponseLiveData.postValue(Response.Error(Constants.NO_INTERNET_CONNECTION))
        }
    }

}

