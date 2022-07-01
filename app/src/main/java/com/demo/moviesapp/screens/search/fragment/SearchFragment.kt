package com.demo.moviesapp.screens.search.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.moviesapp.App
import com.demo.moviesapp.R
import com.demo.moviesapp.commonUtils.Constants
import com.demo.moviesapp.commonUtils.EndlessRecyclerViewScrollListener
import com.demo.moviesapp.commonUtils.IntentContants
import com.demo.moviesapp.commonUtils.alertDialogs.AlertDialogForErrorMsg
import com.demo.moviesapp.commonUtils.alertDialogs.AlertDialogForNoInternetConnectionMessage
import com.demo.moviesapp.commonUtils.alertDialogs.ProgressBarDialog
import com.demo.moviesapp.commonUtils.commonClasses.Response
import com.demo.moviesapp.databinding.FragmentSearchBinding
import com.demo.moviesapp.screens.home.models.Result
import com.demo.moviesapp.screens.search.SearchDetailActivity
import com.demo.moviesapp.screens.search.adapter.SearchAdapter
import com.demo.moviesapp.screens.search.viewModel.searchFragment.SearchFragmentViewModel
import com.demo.moviesapp.screens.search.viewModel.searchFragment.SearchFragmentViewModelFactory
import com.google.gson.Gson
import java.lang.Exception
import java.util.ArrayList
import javax.inject.Inject

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var searchAdapter: SearchAdapter
    lateinit var progressBarDialog: ProgressBarDialog

    lateinit var searchFragmentViewModel: SearchFragmentViewModel

    @Inject
    lateinit var serachFragmentViewModelFactory: SearchFragmentViewModelFactory


    private val itemsArrayList: List<Result> = ArrayList<Result>()
    private val query: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        init()
        setListeners()
        initRecyclerView()

        return binding.root
    }

    /////////////////////////////////////////////////////////
    // Initial Methods
    ////////////////////////////////////////////////////////

    private fun init() {
        progressBarDialog = ProgressBarDialog(requireContext())

        (activity?.application as App).applicationComponent.inject(this)

        searchFragmentViewModel = ViewModelProvider(this, serachFragmentViewModelFactory).get(
            SearchFragmentViewModel::class.java
        )

        observeData()

    }

    private fun setListeners() {
        binding.etSearchText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.length > 2) {
                    showProgressBar()
                    searchFragmentViewModel.getSearchedMovies(s.toString(), 1)
                }
            }

        })

        binding.ivCloseBTN.setOnClickListener {
            binding.etSearchText.setText("")
        }

    }

    private fun initRecyclerView() {
        searchAdapter =
            SearchAdapter(context,
                itemsArrayList.toMutableList(),
                object : SearchAdapter.OnOptionClickListener {
                    override fun onMovieItemClick(movie: Result) {

                        var intent = Intent(requireContext(), SearchDetailActivity::class.java)
                        intent.putExtra(IntentContants.MOVIE_ID, movie.id)
                        startActivity(intent)

                    }
                })

        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recViewSearchList.setLayoutManager(linearLayoutManager)

        val scrollListener: EndlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
                override fun onLoadMore(page: Int) {
                    showProgressBar()
                    searchFragmentViewModel.getSearchedMovies(query, page)
                }
            }


        binding.recViewSearchList.addOnScrollListener(scrollListener)
        binding.recViewSearchList.setAdapter(searchAdapter)
    }


    /////////////////////////////////////////////////////////
    // Observe data changes
    ////////////////////////////////////////////////////////

    fun observeData() {
        searchFragmentViewModel.searchMovie.observe(viewLifecycleOwner, {
            dismissProgressBar()
            when (it) {
                is Response.Loading -> {
                }
                is Response.Success -> {
                    it.data?.let { it1 ->
                        if (it1.results.size == 0) {
                            binding.emptyLayout.emptyView.visibility = View.VISIBLE
                        }
                        searchAdapter.addMoviesList(it1.results.toMutableList())
                    }

                }
                is Response.Error -> {
                    var errorMsg = Gson().toJson(it)
                    if (errorMsg.contains(Constants.NO_INTERNET_CONNECTION)) {

                        val dialog: DialogFragment = AlertDialogForNoInternetConnectionMessage()
                        dialog.show(childFragmentManager, "dialog")

                    } else {
                        val dialog: DialogFragment = AlertDialogForErrorMsg()
                        dialog.show(childFragmentManager, "dialog")
                    }
                }
            }
        })
    }

    //////////////////////////////////////////////////////////
    // ProgressBar
    //////////////////////////////////////////////////////////
    private fun showProgressBar() {
        try {
            if (!requireActivity().isFinishing) {
                if (progressBarDialog != null) {
                    progressBarDialog.show()
                }
            }
        } catch (ignore: Exception) {
        }
    }


    private fun dismissProgressBar() {
        try {
            if (!requireActivity().isFinishing) {
                if (progressBarDialog != null) {
                    progressBarDialog.dismiss()
                }
            }
        } catch (ignore: Exception) {
        }
    }


}
