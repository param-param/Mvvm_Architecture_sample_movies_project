package com.demo.moviesapp.screens.movieDetail.models

data class MovieDetailResponse(
    val backdrop_path: String,
    val id: Int,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
)