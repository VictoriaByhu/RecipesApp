package com.victoria.recipes

import android.app.Application
import com.victoria.recipes.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class RecipesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@RecipesApplication)
        }
    }
}
