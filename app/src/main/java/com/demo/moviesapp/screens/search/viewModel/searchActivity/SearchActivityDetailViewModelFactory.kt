package com.demo.moviesapp.screens.search.viewModel.searchActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.moviesapp.screens.search.repository.SearchActivityRepository
import javax.inject.Inject

class SearchActivityDetailViewModelFactory @Inject constructor(private val repository: SearchActivityRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchActivityDetailViewModel(repository) as T
    }

}