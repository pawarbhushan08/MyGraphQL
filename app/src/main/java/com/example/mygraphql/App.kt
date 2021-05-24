package com.example.mygraphql

import android.app.Application
import com.example.mygraphql.di.fragmentModule
import com.example.mygraphql.di.networkModule
import com.example.mygraphql.di.repositoryModule
import com.example.mygraphql.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(
                listOf(networkModule, repositoryModule, viewModelModule, fragmentModule)
            )
        }
    }
}