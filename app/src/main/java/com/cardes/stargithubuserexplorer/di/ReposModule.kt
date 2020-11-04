package com.cardes.stargithubuserexplorer.di

import com.cardes.stargithubuserexplorer.data.repository.ApiGithubUserRepository
import com.cardes.stargithubuserexplorer.data.repository.CachingGithubUserRepository
import com.cardes.stargithubuserexplorer.domain.repository.GithubUserRepository
import dagger.Binds
import dagger.Module

@Module
interface ReposModule {
    @Binds
    @ApiRepo
    fun apiGithubUserRepository(apiGithubUserRepository: ApiGithubUserRepository): GithubUserRepository

    @Binds
    @CachingRepo
    fun cachingGithubUserRepository(cachingGithubUserRepository: CachingGithubUserRepository): GithubUserRepository
}