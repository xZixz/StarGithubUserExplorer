package com.cardes.stargithubuserexplorer.components.githubuserdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.cardes.stargithubuserexplorer.TestSchedulerProvider
import com.cardes.stargithubuserexplorer.common.SchedulerProvider
import com.cardes.stargithubuserexplorer.domain.usecase.GetGithubUserUseCase
import com.cardes.stargithubuserexplorer.generateGithubUser
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GithubUserDetailViewModelImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getGithubUsersUseCase: GetGithubUserUseCase = mock()
    private val schedulerProvider: SchedulerProvider = TestSchedulerProvider()
    private val viewStateObserver: Observer<GithubUserDetailViewModel.ViewState> = mock()

    private val githubUserDetailViewModel =
        GithubUserDetailViewModelImpl(getGithubUsersUseCase, schedulerProvider)

    @Before
    fun setUp() {
        githubUserDetailViewModel.viewState.observeForever(viewStateObserver)
    }

    @After
    fun tearDown() {
        githubUserDetailViewModel.viewState.removeObserver(viewStateObserver)
    }

    @Test
    fun `given get github user use case return a github user successfully, when init, then view state is success`() {
        // given
        val loginName = "superman"
        val superman = generateGithubUser()
        whenever(getGithubUsersUseCase.execute(loginName))
            .thenReturn(Single.just(superman))

        // when
        githubUserDetailViewModel.init(loginName)

        // then
        verify(viewStateObserver).onChanged(argThat { this is GithubUserDetailViewModel.ViewState.Loading })
        verify(viewStateObserver).onChanged(argThat {
            this is GithubUserDetailViewModel.ViewState.Success
                    && this.githubUser == superman
        })
    }

    @Test
    fun `given get github user use case return error, when init, then view state is Error`() {
        // given
        val loginName = "superman"
        val exception = Exception("error message")
        whenever(getGithubUsersUseCase.execute(loginName))
            .thenReturn(Single.error(exception))

        // when
        githubUserDetailViewModel.init(loginName)

        // then
        verify(viewStateObserver).onChanged(argThat { this is GithubUserDetailViewModel.ViewState.Loading })
        verify(viewStateObserver).onChanged(argThat {
            this is GithubUserDetailViewModel.ViewState.Error
                    && this.error == exception
        })
    }
}