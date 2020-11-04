package com.cardes.stargithubuserexplorer.data.repository

import androidx.room.EmptyResultSetException
import com.cardes.stargithubuserexplorer.common.TimeProvider
import com.cardes.stargithubuserexplorer.data.database.dao.GithubUserDao
import com.cardes.stargithubuserexplorer.data.database.entity.GithubUserEntity
import com.cardes.stargithubuserexplorer.data.sharedpref.AppSharedPref
import com.cardes.stargithubuserexplorer.di.ApiRepo
import com.cardes.stargithubuserexplorer.domain.model.GithubUser
import com.cardes.stargithubuserexplorer.domain.repository.GithubUserRepository
import io.reactivex.Single
import javax.inject.Inject

class CachingGithubUserRepository @Inject constructor(
    @ApiRepo private val apiGithubUserRepository: GithubUserRepository,
    private val githubUserDao: GithubUserDao,
    private val timeProvider: TimeProvider,
    private val sharedPref: AppSharedPref
) : GithubUserRepository {
    override fun searchGithubUsers(key: String): Single<List<GithubUser>> {
        return apiGithubUserRepository.searchGithubUsers(key)
    }

    override fun getGithubUser(loginName: String): Single<GithubUser> {
        return if (isCacheExpired(loginName)) {
            fetchGithubUserFromApi(loginName)
        } else {
            githubUserDao.getUser(loginName)
                .map { githubUserEntity ->
                    githubUserEntity.toGithubUser()
                }
                .onErrorResumeNext { error ->
                    if (error is EmptyResultSetException) {
                        fetchGithubUserFromApi(loginName)
                    } else {
                        Single.error(error)
                    }
                }
        }
    }

    private fun fetchGithubUserFromApi(loginName: String): Single<GithubUser> {
        return apiGithubUserRepository.getGithubUser(loginName)
            .doOnSuccess { githubUser ->
                cacheGithubUser(githubUser)
            }
    }

    private fun cacheGithubUser(githubUser: GithubUser) {
        githubUserDao.insert(githubUser.toGithubUserEntity())
        sharedPref.setLatestCachedUpdateTime(githubUser.loginName, timeProvider.getCurrentTime())
    }

    private fun isCacheExpired(loginName: String): Boolean {
        val currentTime = timeProvider.getCurrentTime()
        val lastCacheUpdateTime = sharedPref.getLatestCachedUpdateTime(loginName)
        return currentTime - lastCacheUpdateTime > MAX_CACHE_TIME
    }

    private fun GithubUserEntity.toGithubUser(): GithubUser {
        return GithubUser(
            loginName = loginName,
            name = name,
            avatarUrl = avatarUrl,
            location = location,
            email = email,
            followersNum = followersNum,
            blogUrl = blogUrl,
            bio = bio,
            company = company
        )
    }

    private fun GithubUser.toGithubUserEntity(): GithubUserEntity {
        return GithubUserEntity(
            loginName = loginName,
            name = name,
            avatarUrl = avatarUrl,
            location = location,
            email = email,
            followersNum = followersNum,
            bio = bio,
            blogUrl = blogUrl,
            company = company
        )
    }

    companion object {
        const val MAX_CACHE_TIME = 60 * 60 * 1000L // 1 hour
    }

}