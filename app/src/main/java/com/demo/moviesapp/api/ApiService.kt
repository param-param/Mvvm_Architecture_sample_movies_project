package com.demo.moviesapp.api

import com.demo.moviesapp.screens.home.models.PopularMoviesResponse
import com.demo.moviesapp.screens.home.models.SearchResponse
import com.demo.moviesapp.screens.home.models.TopRatedMoviesResponse
import com.demo.moviesapp.screens.home.models.UpcomingMoviesResponse
import com.demo.moviesapp.screens.movieDetail.models.MovieDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/upcoming")
    suspend fun getUpcomingMoviesList(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Response<UpcomingMoviesResponse>


    @GET("movie/popular")
    suspend fun getPopularMoviesList(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Response<PopularMoviesResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMoviesList(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Response<TopRatedMoviesResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDeatils(
        @Path("movie_id") id: Int,
        @Query("api_key") api_key: String
    ): Response<MovieDetailResponse>

    @GET("search/movie")
    suspend fun getsearchResults(
        @Query("api_key") api_key: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<SearchResponse>


}