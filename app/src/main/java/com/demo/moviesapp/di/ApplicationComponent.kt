package com.demo.moviesapp.di

import com.demo.moviesapp.screens.home.fragment.HomeFragment
import com.demo.moviesapp.screens.movieDetail.MovieDetailActivity
import com.demo.moviesapp.screens.popularAndTopRatedList.fragment.BannerListFragment
import com.demo.moviesapp.screens.popularAndTopRatedList.fragment.ListFragment
import com.demo.moviesapp.screens.search.SearchDetailActivity
import com.demo.moviesapp.screens.search.fragment.SearchFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, AppModule::class])
interface ApplicationComponent {

    fun inject(homeFragment: HomeFragment)
    fun inject(bannerListFragment: BannerListFragment)
    fun inject(listFragment: ListFragment)
    fun inject(searchFragment: SearchFragment)
    fun inject(movieDetailActivity: MovieDetailActivity)
    fun inject(searchDetailActivity: SearchDetailActivity)

}