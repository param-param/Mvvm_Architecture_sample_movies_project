package com.demo.moviesapp.screens.popularAndTopRatedList.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.moviesapp.R
import com.demo.moviesapp.commonUtils.Constants
import com.demo.moviesapp.databinding.ItemViewBannerListMovieBinding
import com.demo.moviesapp.screens.home.models.Result

class BannerListMoviesAdapter(
    private var context: Context?,
    private var movies: MutableList<Result>,
    private val listener: OnOptionClickListener
) :
    RecyclerView.Adapter<BannerListMoviesAdapter.ViewHolder>() {

    fun setMovieList(movies: MutableList<Result>) {

        this.movies.addAll(movies)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemViewBannerListMovieBinding: ItemViewBannerListMovieBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_view_banner_list_movie, parent, false
            )
        return ViewHolder(itemViewBannerListMovieBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        context?.let {
            Glide.with(it)
                .load(Constants.IMAGE_BASE_URL + movie.poster_path)
                .error(R.drawable.no_image)
                .into(holder.binding.ivPoster)
            holder.binding.tvTitle.setText(movie.title)
        }

        holder.binding.ivPoster.setOnClickListener { listener.onMovieItemClick(movie) }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    interface OnOptionClickListener {
        fun onMovieItemClick(movies: Result)
    }

    inner class ViewHolder(itemView: ItemViewBannerListMovieBinding) :
        RecyclerView.ViewHolder(itemView.getRoot()) {
        var binding: ItemViewBannerListMovieBinding

        init {
            binding = itemView
        }
    }
}