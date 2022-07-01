package com.demo.moviesapp.screens.movieDetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.demo.moviesapp.App
import com.demo.moviesapp.commonUtils.alertDialogs.AlertDialogForErrorMsg
import com.demo.moviesapp.commonUtils.commonClasses.Response
import com.demo.moviesapp.databinding.ActivityMovieDetailBinding
import com.demo.moviesapp.screens.home.models.Result
import com.demo.moviesapp.screens.movieDetail.viewModel.MovieDetailActivityViewModel
import com.demo.moviesapp.screens.movieDetail.viewModel.MovieDetailActivityViewModelFactory
import com.demo.moviesapp.screens.popularAndTopRatedList.adapter.ListMoviesAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import java.util.*
import javax.inject.Inject
import androidx.core.widget.NestedScrollView
import com.demo.moviesapp.commonUtils.*
import com.demo.moviesapp.commonUtils.alertDialogs.AlertDialogForNoInternetConnectionMessage
import com.demo.moviesapp.commonUtils.alertDialogs.ProgressBarDialog
import java.lang.Exception


class MovieDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityMovieDetailBinding
    lateinit var listMoviesAdapter: ListMoviesAdapter
    lateinit var progressBarDialog: ProgressBarDialog

    lateinit var movieDetailActivityViewModel: MovieDetailActivityViewModel

    @Inject
    lateinit var movieDetailActivityViewModelFactory: MovieDetailActivityViewModelFactory

    var movieId: Int = 0
    var FLAG_COLLAPSED = true
    lateinit var listType: String
    private val itemsArrayList: List<Result> = ArrayList<Result>()
    var currentPage = 0
    var totalPages = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, com.demo.moviesapp.R.layout.activity_movie_detail)

        Utils.setStatsBarToDark(window)

        if (intent == null) {
            finish()
        }

        movieId = intent.getIntExtra(IntentContants.MOVIE_ID, 0)
        listType = intent.getStringExtra(IntentContants.LIST_TYPE).toString()

        init()
        setListeners()


    }

    /////////////////////////////////////////////////////////
    // Initial Methods
    ////////////////////////////////////////////////////////

    private fun init() {

        progressBarDialog = ProgressBarDialog(this)

        initRecyclerView()

        (application as App).applicationComponent.inject(this)

        movieDetailActivityViewModel =
            ViewModelProvider(this, movieDetailActivityViewModelFactory).get(
                MovieDetailActivityViewModel::class.java
            )

        showProgressBar()
        movieDetailActivityViewModel.getMovieDetail(movieId)

        if (!listType.equals("") && listType.equals(IntentContants.BANNER_LIST)) {
            showProgressBar()
            movieDetailActivityViewModel.getUpComingMovies(1)
        } else if (!listType.equals("") && listType.equals(IntentContants.POPULAR_LIST)) {
            showProgressBar()
            movieDetailActivityViewModel.getPopularMovies(1)
        } else {
            showProgressBar()
            movieDetailActivityViewModel.getTopRatedMovies(1)
        }

        observeUpComingMoviesData()
        observePopularMoviesData()
        observeTopRatedMoviesData()
        observeMovieDetailsData()

    }

    private fun setListeners() {

        var isShow = true
        var scrollRange = -1
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                binding.toolbarLayout.title = "See All"
                isShow = true
                FLAG_COLLAPSED = true;
                binding.ivBackToolbar.visibility = View.VISIBLE
            } else if (isShow) {
                binding.toolbarLayout.title =
                    " " //careful there should a space between double quote otherwise it wont work
                isShow = false
                FLAG_COLLAPSED = false;
                binding.ivBackToolbar.visibility = View.GONE
            }
        })

        binding.llBack.setOnClickListener {
            finish()
        }

        binding.ivBackToolbar.setOnClickListener {
            finish()
        }

    }

    private fun initRecyclerView() {
        listMoviesAdapter =
            ListMoviesAdapter(this,
                itemsArrayList.toMutableList(),
                object : ListMoviesAdapter.OnOptionClickListener {
                    override fun onMovieItemClick(movie: Result) {
                        var intent =
                            Intent(this@MovieDetailActivity, MovieDetailActivity::class.java)
                        intent.putExtra(IntentContants.LIST_TYPE, IntentContants.BANNER_LIST)
                        intent.putExtra(IntentContants.MOVIE_ID, movie.id)
                        startActivity(intent)

                        finish()
                    }
                })

        val gridLayoutManager = GridLayoutManager(this, 2)
        binding.recViewMoviesList.setLayoutManager(gridLayoutManager)

        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                if (currentPage < totalPages) {
                    if (!listType.equals("") && listType.equals(IntentContants.BANNER_LIST)) {
                        showProgressBar()
                        movieDetailActivityViewModel.getUpComingMovies(currentPage + 1)
                    } else if (!listType.equals("") && listType.equals(IntentContants.POPULAR_LIST)) {
                        showProgressBar()
                        movieDetailActivityViewModel.getPopularMovies(currentPage + 1)
                    } else {
                        showProgressBar()
                        movieDetailActivityViewModel.getTopRatedMovies(currentPage + 1)
                    }
                }
            }
        })

        binding.recViewMoviesList.setAdapter(listMoviesAdapter)
    }


    /////////////////////////////////////////////////////////
    // Observe data changes
    ////////////////////////////////////////////////////////

    private fun observeUpComingMoviesData() {

        movieDetailActivityViewModel.upComingMovies.observe(this, Observer {
            dismissProgressBar()
            when (it) {
                is Response.Loading -> {
                    Log.e("MyLog", "==>banner loading")
                }
                is Response.Success -> {

                    it.data?.let { it1 ->
                        totalPages = it1.total_pages
                        currentPage = it1.page
                        if (it1.results.size == 0) {
                            binding.emptyLayout.emptyView.visibility = View.VISIBLE
                        }
                        listMoviesAdapter.addMovieList(it1.results.toMutableList())
                    }

                }
                is Response.Error -> {
                    var errorMsg = Gson().toJson(it)
                    if (errorMsg.contains(Constants.NO_INTERNET_CONNECTION)) {

                        val dialog: DialogFragment = AlertDialogForNoInternetConnectionMessage()
                        dialog.show(supportFragmentManager, "dialog")

                    } else {
                        val dialog: DialogFragment = AlertDialogForErrorMsg()
                        dialog.show(supportFragmentManager, "dialog")
                    }
                }
            }
        })
    }

    private fun observePopularMoviesData() {

        movieDetailActivityViewModel.popularMovies.observe(this, Observer {
            dismissProgressBar()
            when (it) {
                is Response.Loading -> {
                    Log.e("MyLog", "==>banner loading")
                }
                is Response.Success -> {

                    it.data?.let { it1 ->
                        totalPages = it1.total_pages
                        currentPage = it1.page
                        if (it1.results.size == 0) {
                            binding.emptyLayout.emptyView.visibility = View.VISIBLE
                        }
                        listMoviesAdapter.addMovieList(it1.results.toMutableList())
                    }

                }
                is Response.Error -> {
                    var errorMsg = Gson().toJson(it)
                    if (errorMsg.contains(Constants.NO_INTERNET_CONNECTION)) {

                        val dialog: DialogFragment = AlertDialogForNoInternetConnectionMessage()
                        dialog.show(supportFragmentManager, "dialog")

                    } else {
                        val dialog: DialogFragment = AlertDialogForErrorMsg()
                        dialog.show(supportFragmentManager, "dialog")
                    }
                }
            }
        })
    }


    private fun observeTopRatedMoviesData() {

        movieDetailActivityViewModel.topRatedMovies.observe(this, Observer {
            dismissProgressBar()
            when (it) {
                is Response.Loading -> {
                    Log.e("MyLog", "==>banner loading")
                }
                is Response.Success -> {

                    it.data?.let { it1 ->
                        totalPages = it1.total_pages
                        currentPage = it1.page
                        if (it1.results.size == 0) {
                            binding.emptyLayout.emptyView.visibility = View.VISIBLE
                        }
                        listMoviesAdapter.addMovieList(it1.results.toMutableList())
                    }

                }
                is Response.Error -> {
                    var errorMsg = Gson().toJson(it)
                    if (errorMsg.contains(Constants.NO_INTERNET_CONNECTION)) {

                        val dialog: DialogFragment = AlertDialogForNoInternetConnectionMessage()
                        dialog.show(supportFragmentManager, "dialog")

                    } else {
                        val dialog: DialogFragment = AlertDialogForErrorMsg()
                        dialog.show(supportFragmentManager, "dialog")
                    }
                }
            }
        })
    }

    private fun observeMovieDetailsData() {

        movieDetailActivityViewModel.movieDetail.observe(this, Observer {
            dismissProgressBar()
            when (it) {
                is Response.Loading -> {
                }
                is Response.Success -> {

                    it.data?.let { movie ->
                        Glide.with(this)
                            .load(Constants.IMAGE_BASE_URL + movie.poster_path)
                            .error(com.demo.moviesapp.R.drawable.no_image)
                            .into(binding.ivPoster)

                        binding.tvMovieName.setText(movie.title)
                        binding.tvMovieDetail.setText(movie.overview)
                    }

                }
                is Response.Error -> {
                    var errorMsg = Gson().toJson(it)
                    if (errorMsg.contains(Constants.NO_INTERNET_CONNECTION)) {

                        val dialog: DialogFragment = AlertDialogForNoInternetConnectionMessage()
                        dialog.show(supportFragmentManager, "dialog")

                    } else {
                        val dialog: DialogFragment = AlertDialogForErrorMsg()
                        dialog.show(supportFragmentManager, "dialog")
                    }
                }
            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    //////////////////////////////////////////////////////////
    // ProgressBar
    //////////////////////////////////////////////////////////
    private fun showProgressBar() {
        try {
            if (!this.isFinishing) {
                if (progressBarDialog != null) {
                    progressBarDialog.show()
                }
            }
        } catch (ignore: Exception) {
        }
    }


    private fun dismissProgressBar() {
        try {
            if (!this.isFinishing) {
                if (progressBarDialog != null) {
                    progressBarDialog.dismiss()
                }
            }
        } catch (ignore: Exception) {
        }
    }

}

