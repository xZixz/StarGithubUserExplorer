package com.cardes.stargithubuserexplorer.di

import androidx.lifecycle.ViewModelProvider
import com.cardes.stargithubuserexplorer.components.ViewModelFactory
import com.cardes.stargithubuserexplorer.components.githubuserdetail.GithubUserDetailActivity
import com.cardes.stargithubuserexplorer.components.search.SearchActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBindingModule {
    @Binds
    fun viewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector(modules = [SearchActivityModule::class])
    fun searchActivity(): SearchActivity

    @ContributesAndroidInjector(modules = [GithubUserDetailActivityModule::class])
    fun githubUserDetailActivity(): GithubUserDetailActivity
}