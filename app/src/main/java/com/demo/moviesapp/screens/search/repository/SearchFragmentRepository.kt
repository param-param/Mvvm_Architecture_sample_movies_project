package com.demo.moviesapp.screens.search.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.moviesapp.api.ApiService
import com.demo.moviesapp.commonUtils.Constants
import com.demo.moviesapp.commonUtils.Utils
import com.demo.moviesapp.commonUtils.commonClasses.Response
import com.demo.moviesapp.screens.home.models.SearchResponse
import javax.inject.Inject

class SearchFragmentRepository @Inject constructor(
    private val apiService: ApiService,
    private val applicationContext: Context
) {

    private val searchedMovieResponseLiveData = MutableLiveData<Response<SearchResponse>>()
    val searchedMovies: LiveData<Response<SearchResponse>>
        get() = searchedMovieResponseLiveData

    suspend fun getSearchMovies(query: String, page: Int) {

        if (Utils.isInternetAvailable(applicationContext)) {
            try {
                val result = apiService.getsearchResults(Constants.API_KEY, query, page)
                if (result?.body() != null) {
                    searchedMovieResponseLiveData.postValue(Response.Success(result.body()))
                }
            } catch (exception: Exception) {
                searchedMovieResponseLiveData.postValue(Response.Error(exception.message.toString()))
            }
        } else {
            searchedMovieResponseLiveData.postValue(Response.Error(Constants.NO_INTERNET_CONNECTION))
        }
    }

}

