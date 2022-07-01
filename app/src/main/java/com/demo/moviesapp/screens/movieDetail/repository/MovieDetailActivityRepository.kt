package com.demo.moviesapp.screens.movieDetail.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.moviesapp.api.ApiService
import com.demo.moviesapp.commonUtils.Constants
import com.demo.moviesapp.commonUtils.Utils
import com.demo.moviesapp.commonUtils.commonClasses.Response
import com.demo.moviesapp.screens.home.models.PopularMoviesResponse
import com.demo.moviesapp.screens.home.models.TopRatedMoviesResponse
import com.demo.moviesapp.screens.home.models.UpcomingMoviesResponse
import com.demo.moviesapp.screens.movieDetail.models.MovieDetailResponse
import javax.inject.Inject


class MovieDetailActivityRepository @Inject constructor(
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

    private val movieDetailResponseLiveData = MutableLiveData<Response<MovieDetailResponse>>()
    val movieDetail: LiveData<Response<MovieDetailResponse>>
        get() = movieDetailResponseLiveData

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

    suspend fun getMovieDetail(movieId: Int) {

        if (Utils.isInternetAvailable(applicationContext)) {
            try {
                val result = apiService.getMovieDeatils(movieId, Constants.API_KEY)
                if (result?.body() != null) {
                    movieDetailResponseLiveData.postValue(Response.Success(result.body()))
                }
            } catch (exception: Exception) {
                movieDetailResponseLiveData.postValue(Response.Error(exception.message.toString()))
            }
        } else {
            movieDetailResponseLiveData.postValue(Response.Error(Constants.NO_INTERNET_CONNECTION))
        }
    }

}
