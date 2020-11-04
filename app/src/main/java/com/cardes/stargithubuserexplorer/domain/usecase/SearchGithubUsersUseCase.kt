package com.cardes.stargithubuserexplorer.domain.usecase

import com.cardes.stargithubuserexplorer.di.ApiRepo
import com.cardes.stargithubuserexplorer.domain.model.GithubUser
import com.cardes.stargithubuserexplorer.domain.repository.GithubUserRepository
import io.reactivex.Single
import javax.inject.Inject

interface SearchGithubUsersUseCase {
    fun execute(key: String): Single<List<GithubUser>>
}

class SearchGithubUsersUseCaseImpl @Inject constructor(
    @ApiRepo private val githubUserRepository: GithubUserRepository
) : SearchGithubUsersUseCase {
    override fun execute(key: String): Single<List<GithubUser>> {
        return githubUserRepository.searchGithubUsers(key)
    }
}