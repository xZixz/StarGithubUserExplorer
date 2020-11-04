package com.cardes.stargithubuserexplorer.domain.model

data class GithubUser(
    val loginName: String,
    val name: String = "",
    val avatarUrl: String,
    val location: String = "",
    val email: String = "",
    val followersNum: Int = 0,
    val bio: String = "",
    val company: String = "",
    val blogUrl: String = ""
)