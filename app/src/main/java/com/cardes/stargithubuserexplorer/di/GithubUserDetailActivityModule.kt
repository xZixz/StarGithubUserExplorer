package com.cardes.stargithubuserexplorer.di

import androidx.lifecycle.ViewModel
import com.cardes.stargithubuserexplorer.components.githubuserdetail.GithubUserDetailViewModel
import com.cardes.stargithubuserexplorer.components.githubuserdetail.GithubUserDetailViewModelImpl
import com.cardes.stargithubuserexplorer.domain.usecase.GetGithubUserUseCase
import com.cardes.stargithubuserexplorer.domain.usecase.GetGithubUserUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface GithubUserDetailActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(GithubUserDetailViewModel::class)
    fun githubUserDetailViewModel(githubUserDetailViewModel: GithubUserDetailViewModelImpl): ViewModel

    @Binds
    fun getGithubUserUseCase(getGithubUserUseCase: GetGithubUserUseCaseImpl): GetGithubUserUseCase
}