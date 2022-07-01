package com.demo.moviesapp.screens.home.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.moviesapp.App
import com.demo.moviesapp.R
import com.demo.moviesapp.commonUtils.Constants
import com.demo.moviesapp.commonUtils.commonClasses.Response
import com.demo.moviesapp.commonUtils.alertDialogs.AlertDialogForErrorMsg
import com.demo.moviesapp.commonUtils.IntentContants
import com.demo.moviesapp.commonUtils.alertDialogs.AlertDialogForNoInternetConnectionMessage
import com.demo.moviesapp.commonUtils.alertDialogs.ProgressBarDialog
import com.demo.moviesapp.databinding.FragmentHomeBinding
import com.demo.moviesapp.screens.home.adapter.HomeMoviesAdapter
import com.demo.moviesapp.screens.home.adapter.HomeTopBannerAdapter
import com.demo.moviesapp.screens.home.viewModels.HomeViewModel
import com.demo.moviesapp.screens.home.viewModels.HomeViewModelFactory
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.demo.moviesapp.screens.home.models.Result
import com.demo.moviesapp.screens.movieDetail.MovieDetailActivity
import com.google.gson.Gson
import java.lang.Exception
import java.util.ArrayList
import javax.inject.Inject

class HomeFragment : Fragment() {

    lateinit var homeTopBannerAdapter: HomeTopBannerAdapter
    lateinit var homePopularMoviesAdapter: HomeMoviesAdapter
    lateinit var homeTopRatedMoviesAdapter: HomeMoviesAdapter
    lateinit var progressBarDialog: ProgressBarDialog
    lateinit var binding: FragmentHomeBinding
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    private val homeSliderItemsArrayList: List<Result> = ArrayList<Result>()
    private val homePopularItemsArrayList: List<Result> = ArrayList<Result>()
    private val homeTopRatedItemsArrayList: List<Result> = ArrayList<Result>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        init()
        listeners()

        return binding.root
    }

    /////////////////////////////////////////////////////////////
    // Initial Methods
    ////////////////////////////////////////////////////////////

    private fun init() {
        progressBarDialog = ProgressBarDialog(requireContext())

        loadTopBannerSlider()
        loadPopularMovies()
        loadTopRatedMovies()

        (activity?.application as App).applicationComponent.inject(this)

        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        showProgressBar()

        observeDataBannerSlider()
        observeDataPopularMovies()
        observeDataTopRatedMovies()

    }

    private fun listeners() {
        binding.tvBannnerSeeAll.setOnClickListener {
            val action: NavDirections = HomeFragmentDirections
                .actionHomeFragmentToBannerListFragment().setListType(IntentContants.BANNER_LIST)
            Navigation.findNavController(binding.root).navigate(action)
        }
    }


    private fun loadTopBannerSlider() {
        homeTopBannerAdapter = HomeTopBannerAdapter(
            context,
            homeSliderItemsArrayList,
            object : HomeTopBannerAdapter.OnSliderClickListener {
                override fun onClicked(movies: Result) {
                    var intent = Intent(requireContext(), MovieDetailActivity::class.java)
                    intent.putExtra(IntentContants.LIST_TYPE, IntentContants.BANNER_LIST)
                    intent.putExtra(IntentContants.MOVIE_ID, movies.id)
                    startActivity(intent)
                }
            })

        binding.topBannerSlider.setSliderAdapter(homeTopBannerAdapter)
        binding.topBannerSlider.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.topBannerSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.topBannerSlider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        binding.topBannerSlider.scrollTimeInSec = 3 //set scroll delay in seconds :
        binding.topBannerSlider.startAutoCycle()
        binding.topBannerSlider.scrollTo(0, 0)
    }

    private fun loadPopularMovies() {
        homePopularMoviesAdapter =
            HomeMoviesAdapter(context,
                homePopularItemsArrayList,
                object : HomeMoviesAdapter.OnOptionClickListener {
                    override fun onMovieItemClick(movies: Result) {
                        var intent = Intent(requireContext(), MovieDetailActivity::class.java)
                        intent.putExtra(IntentContants.LIST_TYPE, IntentContants.POPULAR_LIST)
                        intent.putExtra(IntentContants.MOVIE_ID, movies.id)
                        startActivity(intent)
                    }

                    override fun onSellAllClick() {
                        val action: NavDirections = HomeFragmentDirections
                            .actionHomeFragmentToListFragment()
                            .setListType(IntentContants.POPULAR_LIST)
                        Navigation.findNavController(binding.root).navigate(action)
                    }


                })

        binding.recViewPopularMovies.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        binding.recViewPopularMovies.setAdapter(homePopularMoviesAdapter)
    }

    private fun loadTopRatedMovies() {
        homeTopRatedMoviesAdapter =
            HomeMoviesAdapter(context,
                homeTopRatedItemsArrayList,
                object : HomeMoviesAdapter.OnOptionClickListener {
                    override fun onMovieItemClick(movies: Result) {
                        var intent = Intent(requireContext(), MovieDetailActivity::class.java)
                        intent.putExtra(IntentContants.LIST_TYPE, IntentContants.TOP_RATED_LIST)
                        intent.putExtra(IntentContants.MOVIE_ID, movies.id)
                        startActivity(intent)
                    }

                    override fun onSellAllClick() {
                        val action: NavDirections = HomeFragmentDirections
                            .actionHomeFragmentToListFragment()
                            .setListType(IntentContants.TOP_RATED_LIST)
                        Navigation.findNavController(binding.root).navigate(action)
                    }

                })

        binding.recViewTopRatedMovies.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )

        binding.recViewTopRatedMovies.setAdapter(homeTopRatedMoviesAdapter)
    }


    /////////////////////////////////////////////////////////
    // Observe data changes
    ////////////////////////////////////////////////////////

    private fun observeDataBannerSlider() {

        homeViewModel.upComingMovies.observe(viewLifecycleOwner, Observer {
           dismissProgressBar()
            when (it) {
                is Response.Loading -> {
                }
                is Response.Success -> {
                    var movies: MutableList<Result> = mutableListOf()

                    it.data?.let {
                        for (i in 1..5) {
                            movies.add(it.results[i])
                        }
                    }

                    it.data?.let { it1 ->
                        homeTopBannerAdapter.setMovieList(movies)
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

    private fun observeDataPopularMovies() {

        homeViewModel.popularMovies.observe(viewLifecycleOwner, {
            dismissProgressBar()
            when (it) {
                is Response.Loading -> {
                    Log.e("MyLog", "==>popular loading")
                }
                is Response.Success -> {
                    var movies: MutableList<Result> = mutableListOf()

                    it.data?.let {
                        for (i in 1..5) {
                            movies.add(it.results[i])
                        }
                    }

                    it.data?.let { it1 ->
                        homePopularMoviesAdapter.setMovieList(movies)
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


    private fun observeDataTopRatedMovies() {

        homeViewModel.topRatedMovies.observe(viewLifecycleOwner, {
            dismissProgressBar()
            when (it) {
                is Response.Loading -> {
                    Log.e("MyLog", "==>toprated loading")
                }
                is Response.Success -> {
                    var movies: MutableList<Result> = mutableListOf()

                    it.data?.let {
                        for (i in 1..5) {
                            movies.add(it.results[i])
                        }
                    }

                    it.data?.let { it1 ->
                        homeTopRatedMoviesAdapter.setMovieList(movies)
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