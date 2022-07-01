package com.demo.moviesapp.screens.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.demo.moviesapp.App
import com.demo.moviesapp.R
import com.demo.moviesapp.commonUtils.Constants
import com.demo.moviesapp.commonUtils.IntentContants
import com.demo.moviesapp.commonUtils.Utils
import com.demo.moviesapp.commonUtils.alertDialogs.AlertDialogForErrorMsg
import com.demo.moviesapp.commonUtils.alertDialogs.AlertDialogForNoInternetConnectionMessage
import com.demo.moviesapp.commonUtils.alertDialogs.ProgressBarDialog
import com.demo.moviesapp.commonUtils.commonClasses.Response
import com.demo.moviesapp.databinding.ActivitySearchDetailBinding
import com.demo.moviesapp.screens.home.models.Result
import com.demo.moviesapp.screens.search.viewModel.searchActivity.SearchActivityDetailViewModel
import com.demo.moviesapp.screens.search.viewModel.searchActivity.SearchActivityDetailViewModelFactory
import com.google.gson.Gson
import java.lang.Exception
import java.util.ArrayList
import javax.inject.Inject

class SearchDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchDetailBinding
    lateinit var progressBarDialog: ProgressBarDialog

    lateinit var searchActivityDetailViewModel: SearchActivityDetailViewModel

    @Inject
    lateinit var searchActivityDetailViewModelFactory: SearchActivityDetailViewModelFactory

    var movieId: Int = 0
    private val itemsArrayList: List<Result> = ArrayList<Result>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_detail)

        binding =
            DataBindingUtil.setContentView(this, com.demo.moviesapp.R.layout.activity_search_detail)

        Utils.setStatsBarToDark(window)

        if (intent == null) {
            finish()
        }

        movieId = intent.getIntExtra(IntentContants.MOVIE_ID, 0)

        init()

    }


    /////////////////////////////////////////////////////////
    // Initial Methods
    ////////////////////////////////////////////////////////

    private fun init() {

        progressBarDialog = ProgressBarDialog(this)

        (application as App).applicationComponent.inject(this)

        searchActivityDetailViewModel =
            ViewModelProvider(this, searchActivityDetailViewModelFactory).get(
                SearchActivityDetailViewModel::class.java
            )

        showProgressBar()
        searchActivityDetailViewModel.getSearchedMovies(movieId)

        observeMovieDetailsData()

    }


    /////////////////////////////////////////////////////////
    // Observe data changes
    ////////////////////////////////////////////////////////

    private fun observeMovieDetailsData() {

        searchActivityDetailViewModel.searchMovieDetail.observe(this, Observer {
            dismissProgressBar()
            when (it) {
                is Response.Loading -> {
                }
                is Response.Success -> {

                    it.data?.let { movie ->
                        Glide.with(this)
                            .load(Constants.IMAGE_BASE_URL + movie.poster_path)
                            .error(R.drawable.no_image)
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