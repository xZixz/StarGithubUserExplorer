package com.cardes.stargithubuserexplorer.components.search

import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cardes.stargithubuserexplorer.*
import com.cardes.stargithubuserexplorer.UiTestUtil.Companion.createViewModelFor
import com.cardes.stargithubuserexplorer.common.NavLiveEvent
import com.cardes.stargithubuserexplorer.domain.model.GithubUser
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchActivityTest {

    private val searchViewModel: SearchViewModel = mock()
    private val viewState = MutableLiveData<SearchViewModel.ViewState>()
    private val navEvent = NavLiveEvent()
    private lateinit var activity: SearchActivity

    private val rule = DaggerActivityTestRule(
        SearchActivity::class.java,
        initialTouchMode = true,
        launchActivity = false,
        setupMocks = { searchActivity ->
            searchActivity.viewModelFactory = createViewModelFor(searchViewModel)
        }
    )

    @Before
    fun setUp() {
        whenever(searchViewModel.viewState).thenReturn(viewState)
        whenever(searchViewModel.navEvent).thenReturn(navEvent)
        activity = rule.launchActivity(null)
    }

    @Test
    fun on_launch_show_right_elements() {
        checkViewVisible(R.id.tv_title_github_user)
        checkViewVisible(R.id.tv_title_explorer)
        checkViewVisible(R.id.iv_github_logo)
        checkViewVisible(R.id.et_search_key)
        checkViewVisible(R.id.btn_search)
    }

    @Test
    fun loading_state_show_right_elements() {
        viewState.postValue(SearchViewModel.ViewState.Loading)
        SearchScreen.assertLoadingVisibility(true)
        SearchScreen.assertUsersListVisibility(false)
        SearchScreen.assertErrorVisibility(false)
    }

    @Test
    fun no_users_found_state_show_right_elements() {
        viewState.postValue(SearchViewModel.ViewState.Error(SearchViewModel.SearchGithubUserException.EmptyResultException))
        SearchScreen.assertLoadingVisibility(false)
        SearchScreen.assertUsersListVisibility(false)
        SearchScreen.assertCommonErrorVisibility(false)
        SearchScreen.assertNoUsersFoundErrorVisibility(true)
    }

    @Test
    fun no_common_error_state_show_right_elements() {
        viewState.postValue(
            SearchViewModel.ViewState.Error(
                SearchViewModel.SearchGithubUserException.CommonException(
                    Exception("error")
                )
            )
        )
        SearchScreen.assertLoadingVisibility(false)
        SearchScreen.assertUsersListVisibility(false)
        SearchScreen.assertCommonErrorVisibility(true)
        SearchScreen.assertNoUsersFoundErrorVisibility(false)
    }

    @Test
    fun no_success_state_show_right_elements() {
        val superman = UiTestUtil.generateGithubUser()
        val batman = UiTestUtil.generateGithubUser(loginName = "batman")
        viewState.postValue(SearchViewModel.ViewState.UsersFetched(listOf(superman, batman)))
        SearchScreen.assertLoadingVisibility(false)
        SearchScreen.assertCommonErrorVisibility(false)
        SearchScreen.assertNoUsersFoundErrorVisibility(false)
        SearchScreen.assertUsersListVisibility(true)
        SearchScreen.assertUsersListShownUsers(listOf(superman, batman))
    }

    class SearchScreen {
        companion object {
            fun assertUsersListVisibility(visible: Boolean) {
                if (visible) {
                    checkViewVisible(R.id.rv_github_users)
                } else {
                    checkViewNotVisible(R.id.rv_github_users)
                }
            }

            fun assertUsersListShownUsers(githubUsers: List<GithubUser>) {
                githubUsers.forEach { githubUser ->
                    checkViewWithTextVisible(githubUser.loginName)
                }
            }

            fun assertLoadingVisibility(visible: Boolean) {
                if (visible) {
                    checkViewVisible(R.id.tv_loading_description)
                    checkViewVisible(R.id.pb_search_users)
                } else {
                    checkViewNotVisible(R.id.tv_loading_description)
                    checkViewNotVisible(R.id.pb_search_users)
                }
            }

            fun assertCommonErrorVisibility(visible: Boolean) {
                if (visible) {
                    checkViewVisible(R.id.tv_common_error)
                    checkViewVisible(R.id.iv_alert)
                } else {
                    checkViewNotVisible(R.id.tv_common_error)
                    checkViewNotVisible(R.id.iv_alert)
                }
            }

            fun assertNoUsersFoundErrorVisibility(visible: Boolean) {
                if (visible) {
                    checkViewVisible(R.id.iv_face)
                    checkViewVisible(R.id.tv_no_user_found)
                } else {
                    checkViewNotVisible(R.id.iv_face)
                    checkViewNotVisible(R.id.tv_no_user_found)
                }
            }

            fun assertErrorVisibility(visible: Boolean) {
                assertNoUsersFoundErrorVisibility(visible)
                assertCommonErrorVisibility(visible)
            }
        }
    }
}