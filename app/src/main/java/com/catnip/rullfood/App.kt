package com.catnip.rullfood

import android.app.Application
import com.catnip.rullfood.data.local.database.AppDatabase
import com.catnip.rullfood.di.AppModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)
        initKoin()
    }
    private fun initKoin() {
        startKoin() {
            androidLogger()
            androidContext(this@App)
            modules(AppModules.modules)
        }
    }
}
