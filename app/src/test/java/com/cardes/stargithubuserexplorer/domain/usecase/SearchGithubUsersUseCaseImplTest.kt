package com.cardes.stargithubuserexplorer.domain.usecase

import com.cardes.stargithubuserexplorer.domain.repository.GithubUserRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class SearchGithubUsersUseCaseImplTest {
    private val githubUserRepository: GithubUserRepository = mock()
    private val searchGithubUsersUseCase = SearchGithubUsersUseCaseImpl(githubUserRepository)

    @Test
    fun `when search github users use case executes, github user repository invokes searchGithubUsers`() {
        val key = "search key"
        searchGithubUsersUseCase.execute(key)
        verify(githubUserRepository).searchGithubUsers(key)
    }
}