package com.cardes.stargithubuserexplorer.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CachingRepo

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiRepo
