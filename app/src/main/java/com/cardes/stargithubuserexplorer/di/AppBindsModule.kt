package com.cardes.stargithubuserexplorer.di

import com.cardes.stargithubuserexplorer.common.NetworkHelper
import com.cardes.stargithubuserexplorer.common.NetworkHelperImpl
import com.cardes.stargithubuserexplorer.common.TimeProvider
import com.cardes.stargithubuserexplorer.common.TimeProviderImpl
import com.cardes.stargithubuserexplorer.data.sharedpref.AppSharedPref
import com.cardes.stargithubuserexplorer.data.sharedpref.AppSharedPrefImpl
import dagger.Binds
import dagger.Module

@Module
interface AppBindsModule {
    @Binds
    fun sharedPref(sharedPref: AppSharedPrefImpl): AppSharedPref

    @Binds
    fun timeProvider(timeProvider: TimeProviderImpl): TimeProvider

    @Binds
    fun networkHelper(networkHelper: NetworkHelperImpl): NetworkHelper
}