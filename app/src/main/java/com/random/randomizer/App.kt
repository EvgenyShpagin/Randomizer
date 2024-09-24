package com.random.randomizer

import android.app.Application
import com.random.randomizer.di.AppComponent
import com.random.randomizer.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}