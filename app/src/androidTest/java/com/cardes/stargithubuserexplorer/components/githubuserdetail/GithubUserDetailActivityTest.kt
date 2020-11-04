package com.cardes.stargithubuserexplorer.components.githubuserdetail

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cardes.stargithubuserexplorer.*
import com.cardes.stargithubuserexplorer.components.githubuserdetail.GithubUserDetailActivity.Companion.LOGIN_NAME_KEY
import com.cardes.stargithubuserexplorer.domain.model.GithubUser
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GithubUserDetailActivityTest {

    private val githubUserDetailViewModel: GithubUserDetailViewModel = mock()
    private val viewState = MutableLiveData<GithubUserDetailViewModel.ViewState>()

    private val rule = DaggerActivityTestRule(
        GithubUserDetailActivity::class.java,
        initialTouchMode = true,
        launchActivity = false,
        setupMocks = { githubUserDetailActivity ->
            githubUserDetailActivity.viewModelFactory = UiTestUtil.createViewModelFor(githubUserDetailViewModel)
        }
    )

    @Before
    fun setUp() {
        whenever(githubUserDetailViewModel.viewState).thenReturn(viewState)
        val intent = Intent()
            .putExtra(LOGIN_NAME_KEY, "superman")
        rule.launchActivity(intent)
    }

    @Test
    fun on_launch_show_right_elements() {
        verify(githubUserDetailViewModel).init("superman")
        checkViewVisible(R.id.iv_user_avatar)
        checkViewVisible(R.id.bg_user_detail)
    }

    @Test
    fun loading_state_show_right_elements() {
        viewState.postValue(GithubUserDetailViewModel.ViewState.Loading)
        GithubUserDetailScreen.assertLoadingVisibility(true)
        GithubUserDetailScreen.assertErrorVisibility(false)
        GithubUserDetailScreen.assertUserDetailVisibility(false)
    }

    @Test
    fun error_state_show_right_elements_and_pressing_retry_call_getUser_from_view_model() {
        viewState.postValue(GithubUserDetailViewModel.ViewState.Error(Exception("error message")))
        GithubUserDetailScreen.assertLoadingVisibility(false)
        GithubUserDetailScreen.assertErrorVisibility(true)
        GithubUserDetailScreen.assertUserDetailVisibility(false)

        GithubUserDetailScreen.pressRetry()
        verify(githubUserDetailViewModel).getUser()
    }

    @Test
    fun success_state_show_right_user_detail() {
        val superman = UiTestUtil.generateGithubUser()
        viewState.postValue(GithubUserDetailViewModel.ViewState.Success(superman))
        GithubUserDetailScreen.assertLoadingVisibility(false)
        GithubUserDetailScreen.assertErrorVisibility(false)
        GithubUserDetailScreen.assertUserDetailVisibility(true)
        GithubUserDetailScreen.assertUserDetailElementsShownCorrectly(superman)
    }

    class GithubUserDetailScreen {
        companion object {
            fun assertLoadingVisibility(visible: Boolean) {
                R.id.pb_load_user.assertVisibility(visible)
            }

            fun assertErrorVisibility(visible: Boolean) {
                R.id.container_error.assertVisibility(visible)
            }

            fun assertUserDetailVisibility(visible: Boolean) {
                R.id.container_user_detail.assertVisibility(visible)
            }

            fun assertUserDetailElementsShownCorrectly(githubUser: GithubUser) {
                checkViewWithTextVisible(githubUser.loginName)
                checkViewWithTextVisible(githubUser.location)
                checkViewWithTextVisible(githubUser.blogUrl)
                checkViewWithTextVisible(githubUser.name)
                checkViewWithTextVisible(githubUser.company)
            }

            fun pressRetry() {
                onView(withId(R.id.btn_retry)).perform(click())
            }
        }
    }
}