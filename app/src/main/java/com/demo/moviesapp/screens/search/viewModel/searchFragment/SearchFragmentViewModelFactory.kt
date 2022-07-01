package com.demo.moviesapp.screens.search.viewModel.searchFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.moviesapp.screens.search.repository.SearchFragmentRepository
import javax.inject.Inject


class SearchFragmentViewModelFactory @Inject constructor(private val repository: SearchFragmentRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchFragmentViewModel(repository) as T
    }

}