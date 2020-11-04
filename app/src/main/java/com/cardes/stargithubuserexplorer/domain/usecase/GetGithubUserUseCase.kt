package com.cardes.stargithubuserexplorer.domain.usecase

import com.cardes.stargithubuserexplorer.di.CachingRepo
import com.cardes.stargithubuserexplorer.domain.model.GithubUser
import com.cardes.stargithubuserexplorer.domain.repository.GithubUserRepository
import io.reactivex.Single
import javax.inject.Inject

interface GetGithubUserUseCase {
    fun execute(loginName: String): Single<GithubUser>
}

class GetGithubUserUseCaseImpl @Inject constructor(
    @CachingRepo private val githubUserRepository: GithubUserRepository
) : GetGithubUserUseCase {
    override fun execute(loginName: String): Single<GithubUser> {
        return githubUserRepository.getGithubUser(loginName)
    }
}
