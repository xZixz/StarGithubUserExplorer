package com.cardes.stargithubuserexplorer.data.api

import com.google.gson.annotations.SerializedName

data class SearchGithubUsersResponse(
    @SerializedName("total_count")
    val totalCount: Int,

    @SerializedName("items")
    val items: List<SearchUserResponse>
)

data class SearchUserResponse(
    val id: Int,

    val login: String,

    @SerializedName("avatar_url")
    val avatarUrl: String
)