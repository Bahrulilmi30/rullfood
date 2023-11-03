package com.catnip.rullfood

import android.app.Application
import com.catnip.rullfood.data.database.AppDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)
//        initKoin()
    }
//    private fun initKoin() {
//        GlobalContext.startKoin() {
//            androidLogger()
//            androidContext(this@App)
//            modules(AppModule.modules)
//        }
//    }
}
