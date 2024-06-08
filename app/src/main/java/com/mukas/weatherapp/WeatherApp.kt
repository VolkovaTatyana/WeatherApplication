package com.mukas.weatherapp

import android.app.Application
import com.mukas.weatherapp.di.databaseModule
import com.mukas.weatherapp.di.networkModule
import com.mukas.weatherapp.di.repositoryModule
import com.mukas.weatherapp.di.useCaseModule
import com.mukas.weatherapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@WeatherApp)
            modules(networkModule, databaseModule, repositoryModule, useCaseModule, viewModelModule)
        }
    }
}