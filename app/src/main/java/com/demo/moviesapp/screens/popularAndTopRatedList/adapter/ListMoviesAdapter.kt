package com.demo.moviesapp.screens.popularAndTopRatedList.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.moviesapp.R
import com.demo.moviesapp.commonUtils.Constants
import com.demo.moviesapp.databinding.ItemViewMoviesListBinding
import com.demo.moviesapp.screens.home.models.Result

class ListMoviesAdapter(
    private var context: Context?,
    private var movies: MutableList<Result>,
    private val listener: OnOptionClickListener
) :
    RecyclerView.Adapter<ListMoviesAdapter.ViewHolder>() {

    fun addMovieList(movies: MutableList<Result>) {

        this.movies.addAll(movies)

        notifyDataSetChanged()
    }

    fun setMovieList(movies: MutableList<Result>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemViewMoviesListBinding: ItemViewMoviesListBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_view_movies_list, parent, false
            )
        return ViewHolder(itemViewMoviesListBinding)

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

        holder.binding.llImageContainer.setOnClickListener {
            listener.onMovieItemClick(movie)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    interface OnOptionClickListener {
        fun onMovieItemClick(movie: Result)
    }

    inner class ViewHolder(itemView: ItemViewMoviesListBinding) :
        RecyclerView.ViewHolder(itemView.getRoot()) {
        var binding: ItemViewMoviesListBinding

        init {
            binding = itemView
        }
    }
}