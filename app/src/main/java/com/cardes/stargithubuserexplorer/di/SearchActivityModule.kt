package com.cardes.stargithubuserexplorer.di

import androidx.lifecycle.ViewModel
import com.cardes.stargithubuserexplorer.components.search.SearchViewModelImpl
import com.cardes.stargithubuserexplorer.components.search.SearchViewModel
import com.cardes.stargithubuserexplorer.domain.usecase.SearchGithubUsersUseCase
import com.cardes.stargithubuserexplorer.domain.usecase.SearchGithubUsersUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SearchActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun searchViewModel(searchViewModel: SearchViewModelImpl): ViewModel

    @Binds
    fun searchGithubUsersUseCase(searchGithubUsersUseCase: SearchGithubUsersUseCaseImpl): SearchGithubUsersUseCase
}