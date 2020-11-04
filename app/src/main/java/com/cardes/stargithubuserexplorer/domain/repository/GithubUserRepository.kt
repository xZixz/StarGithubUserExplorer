package com.cardes.stargithubuserexplorer.domain.repository

import com.cardes.stargithubuserexplorer.domain.model.GithubUser
import io.reactivex.Single

interface GithubUserRepository {
    fun searchGithubUsers(key: String): Single<List<GithubUser>>
    fun getGithubUser(loginName: String): Single<GithubUser>
}