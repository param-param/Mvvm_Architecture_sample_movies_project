package com.demo.moviesapp

import android.app.Application
import com.demo.moviesapp.di.AppModule
import com.demo.moviesapp.di.ApplicationComponent
import com.demo.moviesapp.di.DaggerApplicationComponent

class App : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .build()
    }


}