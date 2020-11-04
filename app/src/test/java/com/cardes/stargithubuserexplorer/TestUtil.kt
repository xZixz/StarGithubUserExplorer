package com.cardes.stargithubuserexplorer

import com.cardes.stargithubuserexplorer.data.api.GithubUserResponse
import com.cardes.stargithubuserexplorer.data.api.SearchGithubUsersResponse
import com.cardes.stargithubuserexplorer.data.api.SearchUserResponse
import com.cardes.stargithubuserexplorer.data.database.entity.GithubUserEntity
import com.cardes.stargithubuserexplorer.domain.model.GithubUser

fun generateGithubUser(
    loginName: String = "superman",
    name: String = "Clark Kent",
    avatarUrl: String = "https://superman_with_cape_png",
    location: String = "Metropolis",
    email: String = "super.man@awesome.com",
    followersNum: Int = 1_333_333
): GithubUser {
    return GithubUser(
        loginName = loginName,
        name = name,
        avatarUrl = avatarUrl,
        location = location,
        email = email,
        followersNum = followersNum
    )
}

fun generateGithubUserEntity(
    loginName: String = "superman",
    name: String = "Clark Kent",
    avatarUrl: String = "https://image.com/superman_with_cape_png",
    location: String = "Metropolis",
    email: String = "super.man@awesome.com",
    followersNum: Int = 1_333_333,
    bio: String = "Strong but no money",
    company: String = "The Planet",
    blogUrl: String = "https://superman.blog"
): GithubUserEntity {
    return GithubUserEntity(
        loginName = loginName,
        name = name,
        avatarUrl = avatarUrl,
        location = location,
        email = email,
        followersNum = followersNum,
        bio = bio,
        company = company,
        blogUrl = blogUrl
    )
}

fun generateGithubUserResponse(
    id: Int = 0,
    loginName: String = "superman",
    name: String = "Clark Kent",
    avatarUrl: String = "https://image.com/superman_with_cape_png",
    location: String = "Metropolis",
    email: String = "super.man@awesome.com",
    followersNum: Int = 1_333_333,
    bio: String = "Strong but no money",
    company: String = "The Planet",
    blogUrl: String = "https://superman.blog"
): GithubUserResponse {
    return GithubUserResponse(
        id = id,
        login = loginName,
        name = name,
        avatarUrl = avatarUrl,
        location = location,
        email = email,
        followers = followersNum,
        bio = bio,
        company = company,
        blogUrl = blogUrl
    )
}

fun generateGithubSearchUsersResponse(
    totalCount: Int,
    searchUserResponses: List<SearchUserResponse>
): SearchGithubUsersResponse {
    return SearchGithubUsersResponse(
        totalCount = totalCount,
        items = searchUserResponses
    )
}

fun generateSearchUserResponse(
    id: Int = 0,
    login: String = "superman",
    avatarUrl: String = "https://image.com/superman_with_cape.png"
): SearchUserResponse {
    return SearchUserResponse(
        id = id,
        login = login,
        avatarUrl = avatarUrl
    )
}
