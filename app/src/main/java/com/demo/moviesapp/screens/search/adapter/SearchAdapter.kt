package com.demo.moviesapp.screens.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.demo.moviesapp.R
import com.demo.moviesapp.databinding.ItemViewSearchMovieBinding
import com.demo.moviesapp.screens.home.models.Result


class SearchAdapter(
    private var context: Context?,
    private var movies: MutableList<Result>,
    private val listener: OnOptionClickListener
) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    fun addMoviesList(movies: MutableList<Result>) {

        this.movies.addAll(movies)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemViewSearchMovieBinding: ItemViewSearchMovieBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_view_search_movie, parent, false
            )
        return ViewHolder(itemViewSearchMovieBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        context?.let {
            holder.binding.tvTitle.setText(movie.title)
        }

        holder.binding.constraintMain.setOnClickListener { listener.onMovieItemClick(movie) }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    interface OnOptionClickListener {
        fun onMovieItemClick(movies: Result)
    }

    inner class ViewHolder(itemView: ItemViewSearchMovieBinding) :
        RecyclerView.ViewHolder(itemView.getRoot()) {
        var binding: ItemViewSearchMovieBinding

        init {
            binding = itemView
        }
    }
}