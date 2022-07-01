package com.demo.moviesapp.screens.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.moviesapp.R
import com.demo.moviesapp.commonUtils.Constants
import com.demo.moviesapp.databinding.ItemViewHomeMovieBinding
import com.demo.moviesapp.databinding.ItemViewSellAllBinding
import com.demo.moviesapp.screens.home.models.Result


class HomeMoviesAdapter(
    private var context: Context?,
    private var movies: List<Result>,
    private val listener: OnOptionClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ITEM = 1
        const val VIEW_TYPE_VIEW_ALL = 2
    }

    fun setMovieList(movies: List<Result>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == VIEW_TYPE_ITEM) {
            val itemViewPeopleHomeDetailBinding: ItemViewHomeMovieBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.item_view_home_movie, parent, false
                )
            return ViewHolder(itemViewPeopleHomeDetailBinding)
        } else {
            val itemViewSellAllBinding: ItemViewSellAllBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.item_view_sell_all, parent, false
                )
            return SeeAllViewHolder(itemViewSellAllBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = movies[position]
        if (getItemViewType(position) == VIEW_TYPE_VIEW_ALL) {

            (holder as SeeAllViewHolder).binding.btnSeeAll.setOnClickListener { listener.onSellAllClick() }

        } else {

            context?.let {
                Glide.with(it)
                    .load(Constants.IMAGE_BASE_URL + movie.poster_path)
                    .error(R.drawable.no_image)
                    .into((holder as ViewHolder).binding.ivPoster)

                (holder as ViewHolder).binding.tvTitle.setText(movie.title)


            }
            (holder as ViewHolder).binding.llImageContainer.setOnClickListener {
                listener.onMovieItemClick(
                    movie
                )
            }

        }

    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 4) {
            return VIEW_TYPE_VIEW_ALL
        } else {
            return VIEW_TYPE_ITEM
        }
    }

    interface OnOptionClickListener {
        fun onMovieItemClick(movies: Result)
        fun onSellAllClick()
    }

    inner class ViewHolder(itemView: ItemViewHomeMovieBinding) :
        RecyclerView.ViewHolder(itemView.getRoot()) {
        var binding: ItemViewHomeMovieBinding

        init {
            binding = itemView
        }
    }


    inner class SeeAllViewHolder(itemView: ItemViewSellAllBinding) :
        RecyclerView.ViewHolder(itemView.getRoot()) {
        var binding: ItemViewSellAllBinding

        init {
            binding = itemView
        }
    }
}