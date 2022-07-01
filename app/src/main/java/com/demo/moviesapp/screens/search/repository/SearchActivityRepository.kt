package com.demo.moviesapp.screens.search.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.moviesapp.api.ApiService
import com.demo.moviesapp.commonUtils.Constants
import com.demo.moviesapp.commonUtils.Utils
import com.demo.moviesapp.commonUtils.commonClasses.Response
import com.demo.moviesapp.screens.movieDetail.models.MovieDetailResponse
import javax.inject.Inject

class SearchActivityRepository @Inject constructor(
    private val apiService: ApiService,
    private val applicationContext: Context
) {

    private val searchedMovieDetailResponseLiveData =
        MutableLiveData<Response<MovieDetailResponse>>()
    val searchMovieDetail: LiveData<Response<MovieDetailResponse>>
        get() = searchedMovieDetailResponseLiveData

    suspend fun getSearchMovieDetail(movieId: Int) {

        if (Utils.isInternetAvailable(applicationContext)) {
            try {
                val result = apiService.getMovieDeatils(movieId, Constants.API_KEY)
                if (result?.body() != null) {
                    searchedMovieDetailResponseLiveData.postValue(Response.Success(result.body()))
                }
            } catch (exception: Exception) {
                searchedMovieDetailResponseLiveData.postValue(Response.Error(exception.message.toString()))
            }
        } else {
            searchedMovieDetailResponseLiveData.postValue(Response.Error(Constants.NO_INTERNET_CONNECTION))
        }
    }

}

