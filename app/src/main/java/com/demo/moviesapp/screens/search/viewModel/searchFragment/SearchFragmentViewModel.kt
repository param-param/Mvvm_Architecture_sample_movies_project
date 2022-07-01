package com.demo.moviesapp.screens.search.viewModel.searchFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.moviesapp.commonUtils.commonClasses.Response
import com.demo.moviesapp.screens.home.models.SearchResponse
import com.demo.moviesapp.screens.search.repository.SearchFragmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchFragmentViewModel(private val repository: SearchFragmentRepository) : ViewModel() {

    val searchMovie: LiveData<Response<SearchResponse>>
        get() = repository.searchedMovies


    fun getSearchedMovies(query: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSearchMovies(query, page)
        }
    }

}