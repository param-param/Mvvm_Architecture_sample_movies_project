package com.demo.moviesapp.screens.popularAndTopRatedList.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.moviesapp.App
import com.demo.moviesapp.R
import com.demo.moviesapp.screens.bannerList.viewModel.BannerListFragmentViewModel
import com.demo.moviesapp.screens.bannerList.viewModel.BannerListFragmentViewModelFactory
import com.demo.moviesapp.commonUtils.Constants
import com.demo.moviesapp.commonUtils.EndlessRecyclerViewScrollListener
import com.demo.moviesapp.commonUtils.IntentContants
import com.demo.moviesapp.commonUtils.alertDialogs.AlertDialogForErrorMsg
import com.demo.moviesapp.commonUtils.alertDialogs.AlertDialogForNoInternetConnectionMessage
import com.demo.moviesapp.commonUtils.alertDialogs.ProgressBarDialog
import com.demo.moviesapp.commonUtils.commonClasses.Response
import com.demo.moviesapp.databinding.FragmentBannerListBinding
import com.demo.moviesapp.screens.home.models.Result
import com.demo.moviesapp.screens.movieDetail.MovieDetailActivity
import com.demo.moviesapp.screens.popularAndTopRatedList.adapter.BannerListMoviesAdapter
import com.google.gson.Gson
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class BannerListFragment : Fragment() {

    lateinit var binding: FragmentBannerListBinding
    lateinit var bannerListMoviesAdapter: BannerListMoviesAdapter
    lateinit var progressBarDialog: ProgressBarDialog

    lateinit var bannerListFragmentViewModel: BannerListFragmentViewModel

    @Inject
    lateinit var bannerListFragmentViewModelFactory: BannerListFragmentViewModelFactory

    private val bannerItemsArrayList: List<Result> = ArrayList<Result>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_banner_list, container, false)
        init()
        initListeners()

        initRecyclerView()

        return binding.root
    }

    /////////////////////////////////////////////////////////
    // Initial Methods
    ////////////////////////////////////////////////////////

    private fun init() {
        progressBarDialog = ProgressBarDialog(requireContext())

        (activity?.application as App).applicationComponent.inject(this)

        bannerListFragmentViewModel =
            ViewModelProvider(this, bannerListFragmentViewModelFactory).get(
                BannerListFragmentViewModel::class.java
            )

        showProgressBar()

        observeData()
    }

    private fun initListeners() {
        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
    }

    private fun initRecyclerView() {
        bannerListMoviesAdapter =
            BannerListMoviesAdapter(context,
                bannerItemsArrayList.toMutableList(),
                object : BannerListMoviesAdapter.OnOptionClickListener {
                    override fun onMovieItemClick(movie: Result) {

                        var intent = Intent(requireContext(), MovieDetailActivity::class.java)
                        intent.putExtra(IntentContants.LIST_TYPE, IntentContants.BANNER_LIST)
                        intent.putExtra(IntentContants.MOVIE_ID, movie.id)
                        startActivity(intent)

                    }
                })

        val linearLayoutManager = LinearLayoutManager(requireContext())

        val scrollListener: EndlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
                override fun onLoadMore(page: Int) {
                    bannerListFragmentViewModel.getUpComingMovies(page)
                }
            }

        binding.recViewMoviesList.setLayoutManager(linearLayoutManager)
        binding.recViewMoviesList.addOnScrollListener(scrollListener)
        binding.recViewMoviesList.setAdapter(bannerListMoviesAdapter)
    }

    /////////////////////////////////////////////////////////
    // Observe data changes
    ////////////////////////////////////////////////////////

    private fun observeData() {
        bannerListFragmentViewModel.upComingMovies.observe(viewLifecycleOwner, Observer {
            dismissProgressBar()
            when (it) {
                is Response.Loading -> {
                    Log.e("MyLog", "==>banner loading")
                }
                is Response.Success -> {

                    it.data?.let { it1 ->
                        if (it1.results.size == 0) {
                            binding.emptyLayout.emptyView.visibility = View.VISIBLE
                        }
                        bannerListMoviesAdapter.setMovieList(it1.results.toMutableList())
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