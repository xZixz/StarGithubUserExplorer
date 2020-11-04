package com.cardes.stargithubuserexplorer.components.search

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.cardes.stargithubuserexplorer.TestSchedulerProvider
import com.cardes.stargithubuserexplorer.common.ActivityNavEvent
import com.cardes.stargithubuserexplorer.common.NavEvent
import com.cardes.stargithubuserexplorer.common.SchedulerProvider
import com.cardes.stargithubuserexplorer.components.githubuserdetail.GithubUserDetailActivity
import com.cardes.stargithubuserexplorer.components.githubuserdetail.GithubUserDetailActivity.Companion.LOGIN_NAME_KEY
import com.cardes.stargithubuserexplorer.domain.usecase.SearchGithubUsersUseCase
import com.cardes.stargithubuserexplorer.generateGithubUser
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.verify
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val searchGithubUsersUseCase: SearchGithubUsersUseCase = mock()
    private val schedulerProvider: SchedulerProvider = TestSchedulerProvider()
    private val viewStateObserver: Observer<SearchViewModel.ViewState> = mock()
    private val navEventObserver: Observer<NavEvent?> = mock()

    private val searchViewModel = SearchViewModelImpl(searchGithubUsersUseCase, schedulerProvider)

    @Before
    fun setUp() {
        searchViewModel.viewState.observeForever(viewStateObserver)
        searchViewModel.navEvent.observeForever(navEventObserver)
    }

    @After
    fun tearDown() {
        searchViewModel.viewState.removeObserver(viewStateObserver)
        searchViewModel.navEvent.removeObserver(navEventObserver)
    }

    @Test
    fun `given search github users use case returns a list of users, when search users, then view state is UsersFetched`() {
        // given
        val key = "superman"
        val superman = generateGithubUser()
        val batman = generateGithubUser(loginName = "batman", name = "Bruce Wayne")
        val returnedGithubUsers = listOf(superman, batman)
        whenever(searchGithubUsersUseCase.execute(key)).thenReturn(
            Single.just(returnedGithubUsers)
        )

        // when
        searchViewModel.searchUsers(key)

        // then
        verify(viewStateObserver).onChanged(argThat { this is SearchViewModel.ViewState.Loading })
        verify(viewStateObserver).onChanged(argThat {
            this is SearchViewModel.ViewState.UsersFetched
                    && this.githubUsers == returnedGithubUsers
        })
    }

    @Test
    fun `given search github users use case return an empty list, when search users, then view state is Error with Empty exception`() {
        // given
        val key = "superman"
        whenever(searchGithubUsersUseCase.execute(key)).thenReturn(Single.just(listOf()))

        // when
        searchViewModel.searchUsers(key)

        // then
        verify(viewStateObserver).onChanged(argThat { this is SearchViewModel.ViewState.Loading })
        verify(viewStateObserver).onChanged(argThat {
            this is SearchViewModel.ViewState.Error
                    && this.error is SearchViewModel.SearchGithubUserException.EmptyResultException
        })
    }

    @Test
    fun `given search github users use case return an error, when search users, then view state is Error with Common exception`() {
        // given
        val key = "superman"
        val exception = Exception("error message")
        whenever(searchGithubUsersUseCase.execute(key)).thenReturn(Single.error(exception))

        // when
        searchViewModel.searchUsers(key)

        // then
        verify(viewStateObserver).onChanged(argThat { this is SearchViewModel.ViewState.Loading })
        verify(viewStateObserver).onChanged(argThat {
            this is SearchViewModel.ViewState.Error
                    && this.error is SearchViewModel.SearchGithubUserException.CommonException
                    && (this.error as SearchViewModel.SearchGithubUserException.CommonException).throwable == exception
        })
    }

    @Test
    fun `when on item click, then nav event go to GithubUserDetail activity`() {
        val loginName = "superman"
        mockkConstructor(Bundle::class)

        every {
            anyConstructed<Bundle>()
                .putString(LOGIN_NAME_KEY, loginName)
        } returns Unit


        searchViewModel.onItemClick(loginName)
        verify(navEventObserver).onChanged(argThat {
            this is ActivityNavEvent
                    && this.clazz == GithubUserDetailActivity::class.java
        })

        verify {
            anyConstructed<Bundle>()
                .putString(LOGIN_NAME_KEY, loginName)
        }
    }
}