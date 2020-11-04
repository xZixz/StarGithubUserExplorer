package com.cardes.stargithubuserexplorer.di

import android.app.Application
import com.cardes.stargithubuserexplorer.MyApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        AppBindsModule::class,
        DatabaseModule::class,
        ReposModule::class,
        CommonModule::class,
        NetworkModule::class,
        AndroidInjectionModule::class,
        ActivityBindingModule::class
    ]
)
interface AppComponent : AndroidInjector<MyApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}