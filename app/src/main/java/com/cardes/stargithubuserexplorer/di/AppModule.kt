package com.cardes.stargithubuserexplorer.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule {
    @Provides
    fun context(application: Application): Context = application.applicationContext
}