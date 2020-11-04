package com.cardes.stargithubuserexplorer.domain.usecase

import com.cardes.stargithubuserexplorer.domain.repository.GithubUserRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class GetGithubUserUseCaseImplTest {
    private val githubUserRepository: GithubUserRepository = mock()
    private val getGithubUserUseCase = GetGithubUserUseCaseImpl(githubUserRepository)

    @Test
    fun `when use case executes, then github user repo invokes getGithubUser`() {
        val loginName = "login name"
        getGithubUserUseCase.execute(loginName)
        verify(githubUserRepository).getGithubUser(loginName)
    }

}