package com.cardes.stargithubuserexplorer

import android.app.Application
import com.cardes.stargithubuserexplorer.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class MyApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        val component = DaggerAppComponent
            .builder()
            .application(this)
            .build()

        component.inject(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}