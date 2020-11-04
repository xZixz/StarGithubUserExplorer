package com.cardes.stargithubuserexplorer.data.repository

import androidx.room.EmptyResultSetException
import com.cardes.stargithubuserexplorer.common.TimeProvider
import com.cardes.stargithubuserexplorer.data.database.dao.GithubUserDao
import com.cardes.stargithubuserexplorer.data.repository.CachingGithubUserRepository.Companion.MAX_CACHE_TIME
import com.cardes.stargithubuserexplorer.data.sharedpref.AppSharedPref
import com.cardes.stargithubuserexplorer.domain.repository.GithubUserRepository
import com.cardes.stargithubuserexplorer.generateGithubUser
import com.cardes.stargithubuserexplorer.generateGithubUserEntity
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Test

class CachingGithubUserRepositoryTest {
    private val githubUserDao: GithubUserDao = mock()
    private val apiGithubUserRepository: GithubUserRepository = mock()
    private val timeProvider: TimeProvider = mock()
    private val sharedPref: AppSharedPref = mock()
    private val cachingGithubUserRepository = CachingGithubUserRepository(
        apiGithubUserRepository,
        githubUserDao,
        timeProvider,
        sharedPref
    )

    @Test
    fun `given caching time is expired, when get github user, api repository is called and cache data after success`() {
        // given caching time is expired
        val userLoginName = "superman"
        val githubUser = generateGithubUser(loginName = userLoginName)
        whenever(timeProvider.getCurrentTime()).thenReturn(1_000_000)
        whenever(sharedPref.getLatestCachedUpdateTime(userLoginName)).thenReturn(1_000_000 - MAX_CACHE_TIME - 1_000)

        whenever(apiGithubUserRepository.getGithubUser(userLoginName))
            .thenReturn(Single.just(githubUser))

        // when
        cachingGithubUserRepository.getGithubUser(userLoginName)
            .test()

        // then
        verify(apiGithubUserRepository).getGithubUser(userLoginName)
        verify(githubUserDao).insert(argThat {
            loginName == userLoginName
                    && name == githubUser.name
                    && avatarUrl == githubUser.avatarUrl
                    && location == githubUser.location
                    && email == githubUser.email
                    && followersNum == githubUser.followersNum
        })
        verify(sharedPref).setLatestCachedUpdateTime(userLoginName, 1_000_000)
    }

    @Test
    fun `given caching time is not expired but can not get github user from database, when get github user, then execute the procedure of fetching user from api`() {
        // given
        // caching time is not expired
        val userLoginName = "superman"
        val githubUser = generateGithubUser(loginName = userLoginName)
        whenever(timeProvider.getCurrentTime()).thenReturn(1_000_000)
        whenever(sharedPref.getLatestCachedUpdateTime(userLoginName)).thenReturn(1_000_000 - MAX_CACHE_TIME + 1_000)

        // github user can not fetched from database
        whenever(githubUserDao.getUser(userLoginName))
            .thenReturn(Single.error(EmptyResultSetException("error message")))

        whenever(apiGithubUserRepository.getGithubUser(userLoginName))
            .thenReturn(Single.just(githubUser))

        // when
        cachingGithubUserRepository.getGithubUser(userLoginName)
            .test()

        //then
        verify(apiGithubUserRepository).getGithubUser(userLoginName)
        verify(githubUserDao).insert(argThat {
            loginName == userLoginName
                    && name == githubUser.name
                    && avatarUrl == githubUser.avatarUrl
                    && location == githubUser.location
                    && email == githubUser.email
                    && followersNum == githubUser.followersNum
        })
        verify(sharedPref).setLatestCachedUpdateTime(userLoginName, 1_000_000)
    }

    @Test
    fun `given caching time is not expired and user is fetched from database, when get github user, then return the correct github user`() {

        // given
        // caching time is not expired
        val userLoginName = "superman"
        whenever(timeProvider.getCurrentTime()).thenReturn(1_000_000)
        whenever(sharedPref.getLatestCachedUpdateTime(userLoginName)).thenReturn(1_000_000 - MAX_CACHE_TIME + 1_000)

        // github user is fetched from database
        val githubUserEntity = generateGithubUserEntity(loginName = userLoginName)
        whenever(githubUserDao.getUser(userLoginName))
            .thenReturn(Single.just(githubUserEntity))

        // when
        cachingGithubUserRepository.getGithubUser(userLoginName)
            .test()
            .assertValue {
                it.loginName == githubUserEntity.loginName
                        && it.name == githubUserEntity.name
                        && it.avatarUrl == githubUserEntity.avatarUrl
                        && it.email == githubUserEntity.email
                        && it.location == githubUserEntity.location
                        && it.followersNum == githubUserEntity.followersNum
            }

        verify(sharedPref, never()).setLatestCachedUpdateTime(any(), any())
        verify(githubUserDao, never()).insert(any())

    }
}