package com.cardes.stargithubuserexplorer.data.repository

import com.cardes.stargithubuserexplorer.data.api.GithubApi
import com.cardes.stargithubuserexplorer.data.api.GithubUserResponse
import com.cardes.stargithubuserexplorer.data.api.SearchGithubUsersResponse
import com.cardes.stargithubuserexplorer.data.api.SearchUserResponse
import com.cardes.stargithubuserexplorer.domain.model.GithubUser
import com.cardes.stargithubuserexplorer.domain.repository.GithubUserRepository
import io.reactivex.Single
import javax.inject.Inject

class ApiGithubUserRepository @Inject constructor(
    private val githubApi: GithubApi
) : GithubUserRepository {
    override fun searchGithubUsers(key: String): Single<List<GithubUser>> {
        return githubApi.getUsers(key)
            .map { searchGithubUsersResponse ->
                searchGithubUsersResponse.toListOfGithubUserModel()
            }
    }

    override fun getGithubUser(loginName: String): Single<GithubUser> {
        return githubApi.getUser(loginName)
            .map { githubUserResponse ->
                githubUserResponse.toModel()
            }
    }

    private fun GithubUserResponse.toModel() : GithubUser {
        return GithubUser(
            loginName = login,
            name = name.orEmpty(),
            avatarUrl = avatarUrl,
            location = location.orEmpty(),
            email = email.orEmpty(),
            followersNum = followers,
            blogUrl = blogUrl.orEmpty(),
            company = company.orEmpty(),
            bio = bio.orEmpty()
        )
    }

    private fun SearchUserResponse.toModel() : GithubUser {
        return GithubUser(
            loginName = login,
            avatarUrl = avatarUrl
        )
    }

    private fun SearchGithubUsersResponse.toListOfGithubUserModel() : List<GithubUser> {
        return items.map { searchUserResponse ->
            searchUserResponse.toModel()
        }
    }
}