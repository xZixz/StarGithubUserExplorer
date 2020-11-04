package com.cardes.stargithubuserexplorer.data.repository

import com.cardes.stargithubuserexplorer.data.api.GithubApi
import com.cardes.stargithubuserexplorer.generateGithubSearchUsersResponse
import com.cardes.stargithubuserexplorer.generateGithubUserResponse
import com.cardes.stargithubuserexplorer.generateSearchUserResponse
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test

class ApiGithubUserRepositoryTest {
    private val githubApi: GithubApi = mock()
    private val apiGithubUserRepository = ApiGithubUserRepository(githubApi)

    @Test
    fun `given fetching user from github api success, when repository get github user, then get the correct data of github user`() {
        // given
        val loginName = "superman"
        val githubUserResponse = generateGithubUserResponse(loginName = loginName)
        whenever(githubApi.getUser(loginName)).thenReturn(Single.just(githubUserResponse))

        // when
        apiGithubUserRepository.getGithubUser(loginName)
            .test()
            .assertValue { githubUser ->
                githubUser.loginName == githubUserResponse.login
                        && githubUser.name == githubUserResponse.name
                        && githubUser.avatarUrl == githubUserResponse.avatarUrl
                        && githubUser.location == githubUserResponse.location
                        && githubUser.followersNum == githubUserResponse.followers
            }

        //then
        verify(githubApi).getUser(loginName)
    }

    @Test
    fun `given fetching user from github api fails, when repository get github user, then emit the error`() {
        // given
        val loginName = "superman"
        val exception = Exception("error message")
        whenever(githubApi.getUser(loginName)).thenReturn(Single.error(exception))

        // when
        apiGithubUserRepository.getGithubUser(loginName)
            .test()
            .assertError(exception)

        //then
        verify(githubApi).getUser(loginName)
    }

    @Test
    fun `given searching users from github api success, when repository search github user, then get correct github users info`() {
        // given
        val searchKey = "man"
        val supermanUserResponse = generateSearchUserResponse()
        val batmanUserResponse = generateSearchUserResponse(
            id = 1,
            login = "batman",
            avatarUrl = "https://image.com/batman_with_bat_mobile.png"
        )
        whenever(githubApi.getUsers(searchKey))
            .thenReturn(
                Single.just(
                    generateGithubSearchUsersResponse(
                        totalCount = 2,
                        searchUserResponses = listOf(supermanUserResponse, batmanUserResponse)
                    )
                )
            )

        // when
        apiGithubUserRepository.searchGithubUsers(searchKey)
            .test()
            .assertValue { githubUsers ->
                githubUsers.size == 2
                        && githubUsers[0].loginName == supermanUserResponse.login
                        && githubUsers[0].avatarUrl == supermanUserResponse.avatarUrl
                        && githubUsers[1].loginName == batmanUserResponse.login
                        && githubUsers[1].avatarUrl == batmanUserResponse.avatarUrl
            }

        //then
        verify(githubApi).getUsers(searchKey)
    }

    @Test
    fun `given searching user from github api fails, when repository search github user, then emit the error`() {
        // given
        val loginName = "superman"
        val exception = Exception("error message")
        whenever(githubApi.getUsers(loginName)).thenReturn(Single.error(exception))

        // when
        apiGithubUserRepository.searchGithubUsers(loginName)
            .test()
            .assertError(exception)

        //then
        verify(githubApi).getUsers(loginName)
    }
}