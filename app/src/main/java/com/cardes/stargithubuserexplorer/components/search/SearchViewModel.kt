package com.cardes.stargithubuserexplorer.components.search

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cardes.stargithubuserexplorer.common.ActivityNavEvent
import com.cardes.stargithubuserexplorer.common.NavLiveEvent
import com.cardes.stargithubuserexplorer.common.SchedulerProvider
import com.cardes.stargithubuserexplorer.components.githubuserdetail.GithubUserDetailActivity
import com.cardes.stargithubuserexplorer.domain.model.GithubUser
import com.cardes.stargithubuserexplorer.domain.usecase.SearchGithubUsersUseCase
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

abstract class SearchViewModel : ViewModel() {
    abstract val viewState: LiveData<SearchViewModel.ViewState>
    abstract val navEvent: NavLiveEvent
    abstract fun searchUsers(key: String)
    abstract fun onItemClick(loginName: String)


    sealed class SearchGithubUserException {
        data class CommonException(val throwable: Throwable) : SearchGithubUserException()
        object EmptyResultException : SearchGithubUserException()
    }

    sealed class ViewState {
        object Idle : ViewState()
        object Loading : ViewState()
        data class UsersFetched(val githubUsers: List<GithubUser>): ViewState()
        data class Error(val error: SearchViewModel.SearchGithubUserException) : ViewState()
    }
}

class SearchViewModelImpl @Inject constructor(
    private val searchGithubUsersUseCase: SearchGithubUsersUseCase,
    private val schedulerProvider: SchedulerProvider
) : SearchViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _viewState = MutableLiveData<ViewState>(ViewState.Idle)

    override val navEvent = NavLiveEvent()

    override val viewState = _viewState

    override fun searchUsers(key: String) {
        _viewState.value = ViewState.Loading

        searchGithubUsersUseCase.execute(key)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe(
                { githubUsers ->
                    if (githubUsers.isEmpty()) {
                        _viewState.value = ViewState.Error(SearchGithubUserException.EmptyResultException)
                    } else {
                        _viewState.value = ViewState.UsersFetched(githubUsers)
                    }
                },
                { error ->
                    _viewState.value = ViewState.Error(SearchGithubUserException.CommonException(error))
                    Timber.e(error)
                })
            .let {
                compositeDisposable.add(it)
            }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    override fun onItemClick(loginName: String) {
        val bundle = Bundle().apply { putString(GithubUserDetailActivity.LOGIN_NAME_KEY, loginName) }
        navEvent.value = ActivityNavEvent(
            GithubUserDetailActivity::class.java,
            bundle
        )
    }
}