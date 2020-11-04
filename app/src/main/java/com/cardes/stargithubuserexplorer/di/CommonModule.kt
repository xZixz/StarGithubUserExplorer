package com.cardes.stargithubuserexplorer.di

import com.cardes.stargithubuserexplorer.common.SchedulerProvider
import com.cardes.stargithubuserexplorer.common.SchedulerProviderImpl
import dagger.Binds
import dagger.Module

@Module
interface CommonModule {
    @Binds
    fun schedulerProvider(schedulerProvider: SchedulerProviderImpl): SchedulerProvider
}