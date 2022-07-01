package com.demo.moviesapp.screens.popularAndTopRatedList.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.moviesapp.App
import com.demo.moviesapp.screens.movieDetail.MovieDetailActivity
import com.demo.moviesapp.R
import com.demo.moviesapp.commonUtils.Constants
import com.demo.moviesapp.commonUtils.IntentContants
import com.demo.moviesapp.commonUtils.alertDialogs.AlertDialogForErrorMsg
import com.demo.moviesapp.commonUtils.alertDialogs.AlertDialogForNoInternetConnectionMessage
import com.demo.moviesapp.commonUtils.alertDialogs.ProgressBarDialog
import com.demo.moviesapp.commonUtils.commonClasses.Response
import com.demo.moviesapp.screens.home.models.Result
import com.demo.moviesapp.databinding.FragmentListBinding
import com.demo.moviesapp.screens.popularAndTopRatedList.adapter.ListMoviesAdapter
import com.demo.moviesapp.screens.popularAndTopRatedList.viewModel.popular.PopularMoviesViewModel
import com.demo.moviesapp.screens.popularAndTopRatedList.viewModel.popular.PopularMoviesViewModelFactory
import com.demo.moviesapp.screens.popularAndTopRatedList.viewModel.topRated.TopRatedMoviesViewModel
import com.demo.moviesapp.screens.popularAndTopRatedList.viewModel.topRated.TopRatedMoviesViewModelFactory
import com.google.gson.Gson
import java.lang.Exception
import java.util.ArrayList
import javax.inject.Inject

class ListFragment : Fragment() {

    lateinit var binding: FragmentListBinding
    lateinit var listMoviesAdapter: ListMoviesAdapter
    lateinit var progressBarDialog: ProgressBarDialog
    lateinit var popularMoviesViewModel: PopularMoviesViewModel
    lateinit var topRatedMoviesViewModel: TopRatedMoviesViewModel

    @Inject
    lateinit var popularMoviesViewModelFactory: PopularMoviesViewModelFactory

    @Inject
    lateinit var topRatedMoviesViewModelFactory: TopRatedMoviesViewModelFactory

    private val itemsArrayList: List<Result> = ArrayList<Result>()
    private lateinit var listType: String
    var currentPage = 0
    var totalPages = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)

        if (arguments == null) {
            Navigation.findNavController(binding.root).popBackStack()
        }

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

        listType = ListFragmentArgs.fromBundle(requireArguments()).listType

        if (!listType.equals("") && listType.equals(IntentContants.POPULAR_LIST)) {

            binding.tvTitle.setText(getString(R.string.popular_movies))

            popularMoviesViewModel = ViewModelProvider(
                this,
                popularMoviesViewModelFactory
            ).get(PopularMoviesViewModel::class.java)

            showProgressBar()
            observePopularMoviesData()
        } else {

            binding.tvTitle.setText(getString(R.string.top_rated_movies))

            topRatedMoviesViewModel = ViewModelProvider(
                this,
                topRatedMoviesViewModelFactory
            ).get(TopRatedMoviesViewModel::class.java)

            showProgressBar()
            observeTopRatedMoviesData()
        }

    }


    private fun setListeners() {
        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
    }


    private fun initRecyclerView() {
        listMoviesAdapter =
            ListMoviesAdapter(context,
                itemsArrayList.toMutableList(),
                object : ListMoviesAdapter.OnOptionClickListener {
                    override fun onMovieItemClick(movie: Result) {

                        var intent = Intent(requireContext(), MovieDetailActivity::class.java)
                        if (!listType.equals("") && listType.equals(IntentContants.POPULAR_LIST)) {
                            intent.putExtra(IntentContants.LIST_TYPE, IntentContants.POPULAR_LIST)
                        } else {
                            intent.putExtra(IntentContants.LIST_TYPE, IntentContants.TOP_RATED_LIST)
                        }
                        intent.putExtra(IntentContants.MOVIE_ID, movie.id)
                        startActivity(intent)

                    }
                })

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.recViewMoviesList.setLayoutManager(gridLayoutManager)

        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                if (currentPage < totalPages) {
                    if (!listType.equals("") && listType.equals(IntentContants.POPULAR_LIST)) {
                        showProgressBar()
                        popularMoviesViewModel.getPopularMovies(currentPage + 1)
                    } else {
                        showProgressBar()
                        topRatedMoviesViewModel.getTopRatedMovies(currentPage + 1)
                    }
                }
            }
        })
        binding.recViewMoviesList.setAdapter(listMoviesAdapter)
    }


    /////////////////////////////////////////////////////////
    // Observe data changes
    ////////////////////////////////////////////////////////

    private fun observePopularMoviesData() {
        popularMoviesViewModel.popularMovies.observe(viewLifecycleOwner, {
            dismissProgressBar()
            when (it) {
                is Response.Loading -> {
                }
                is Response.Success -> {
                    it.data?.let { it1 ->
                        totalPages = it1.total_pages
                        currentPage = it1.page
                        listMoviesAdapter.addMovieList(it1.results.toMutableList())
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

    private fun observeTopRatedMoviesData() {
        topRatedMoviesViewModel.topRatedMovies.observe(viewLifecycleOwner, {
            dismissProgressBar()
            when (it) {
                is Response.Loading -> {
                }
                is Response.Success -> {

                    it.data?.let { it1 ->
                        totalPages = it1.total_pages
                        currentPage = it1.page
                        listMoviesAdapter.addMovieList(it1.results.toMutableList())
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