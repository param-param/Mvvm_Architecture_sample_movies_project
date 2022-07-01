package com.demo.moviesapp.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.demo.moviesapp.R
import com.demo.moviesapp.commonUtils.Utils
import com.demo.moviesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        Utils.setStatsBarToDark(window)

        navController = Navigation.findNavController(this, R.id.nav_fragment)
        NavigationUI.setupWithNavController(binding.bottomNavigatinView, navController)

    }

}