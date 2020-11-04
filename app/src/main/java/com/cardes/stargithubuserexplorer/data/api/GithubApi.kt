package com.cardes.stargithubuserexplorer.data.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    @GET("/search/users")
    fun getUsers(@Query("q") key: String): Single<SearchGithubUsersResponse>

    @GET("/users/{login}")
    fun getUser(@Path("login") loginName: String): Single<GithubUserResponse>
}