package com.cardes.stargithubuserexplorer.components.githubuserdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cardes.stargithubuserexplorer.common.SchedulerProvider
import com.cardes.stargithubuserexplorer.domain.model.GithubUser
import com.cardes.stargithubuserexplorer.domain.usecase.GetGithubUserUseCase
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

abstract class GithubUserDetailViewModel : ViewModel() {
    abstract val viewState: LiveData<ViewState>
    abstract fun getUser()
    abstract fun init(loginName: String)

    sealed class ViewState {
        object Idle : ViewState()
        object Loading : ViewState()
        data class Success(val githubUser: GithubUser) : ViewState()
        data class Error(val error: Throwable) : ViewState()
    }
}

class GithubUserDetailViewModelImpl @Inject constructor(
    private val getGithubUserUseCase: GetGithubUserUseCase,
    private val schedulerProvider: SchedulerProvider
) : GithubUserDetailViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val _viewState = MutableLiveData<ViewState>(
        ViewState.Idle
    )

    override val viewState: LiveData<ViewState> = _viewState
    private lateinit var loginName: String

    override fun init(loginName: String) {
        if (!this::loginName.isInitialized) {
            this.loginName = loginName
            getUser()
        }
    }

    override fun getUser() {
        _viewState.value = ViewState.Loading
        getGithubUserUseCase.execute(loginName)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({ githubUser ->
                _viewState.value = ViewState.Success(githubUser)
            }, { error ->
                _viewState.value = ViewState.Error(error)
                Timber.e(error)
            })
            .let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}