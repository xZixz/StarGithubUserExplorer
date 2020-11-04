package com.cardes.stargithubuserexplorer.data.api

import com.google.gson.annotations.SerializedName

data class GithubUserResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("login")
    val login: String,

    @SerializedName("name")
    val name: String?,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("location")
    val location: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("followers")
    val followers: Int,

    @SerializedName("blog")
    val blogUrl: String?,

    @SerializedName("company")
    val company: String?,

    @SerializedName("bio")
    val bio: String?,
)