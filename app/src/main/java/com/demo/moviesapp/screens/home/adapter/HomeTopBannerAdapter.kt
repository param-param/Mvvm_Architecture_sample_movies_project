package com.demo.moviesapp.screens.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.demo.moviesapp.R
import com.demo.moviesapp.commonUtils.Constants
import com.smarteist.autoimageslider.SliderViewAdapter
import com.demo.moviesapp.screens.home.models.Result

class HomeTopBannerAdapter(
    private val context: Context?,
    private var movies: List<Result>,
    private var onSliderClickListener: OnSliderClickListener
) : SliderViewAdapter<HomeTopBannerAdapter.ViewHolder>() {

    fun setMovieList(movies: List<Result>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_home_top_banner, null)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val upComingMovie = movies[position]

        context?.let {
            Glide.with(it)
                .load(Constants.IMAGE_BASE_URL + upComingMovie.backdrop_path)
                .error(R.drawable.no_image)
                .into(viewHolder.ivPoster)
            viewHolder.tvMovieName.setText(upComingMovie.title)
            viewHolder.tvReleasedDate.setText(upComingMovie.release_date)
        }
        viewHolder.ivPoster.setOnClickListener { v: View? ->
            onSliderClickListener.onClicked(upComingMovie)
        }
    }

    override fun getCount(): Int {
        return movies.size
    }

    interface OnSliderClickListener {
        fun onClicked(movies: Result)
    }

    class ViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        var ivPoster: ImageView
        var tvMovieName: TextView
        var tvReleasedDate: TextView

        init {
            ivPoster = itemView.findViewById(R.id.iv_Poster)
            tvMovieName = itemView.findViewById(R.id.tv_Movie_Name)
            tvReleasedDate = itemView.findViewById(R.id.tv_Released_Date)
        }
    }
}