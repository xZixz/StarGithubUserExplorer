package com.cardes.stargithubuserexplorer.data.database.entity

import androidx.room.Entity

@Entity(
    tableName = "github_users",
    primaryKeys = ["loginName"]
)
data class GithubUserEntity(
    val loginName: String,
    val name: String,
    val avatarUrl: String,
    val location: String,
    val email: String,
    val followersNum: Int,
    val bio: String,
    val company: String,
    val blogUrl: String
)