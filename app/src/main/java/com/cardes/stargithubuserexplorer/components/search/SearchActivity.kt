package com.cardes.stargithubuserexplorer.components.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cardes.stargithubuserexplorer.R
import com.cardes.stargithubuserexplorer.common.ActivityNavEvent
import com.cardes.stargithubuserexplorer.common.gone
import com.cardes.stargithubuserexplorer.common.visible
import com.cardes.stargithubuserexplorer.domain.model.GithubUser
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_search.*
import timber.log.Timber
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), GithubUsersAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var searchViewModel: SearchViewModel

    lateinit var githubUsersAdapter: GithubUsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)
        Timber.v("Density: ${resources.displayMetrics.density}")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initViews()

        searchViewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)

        observeViewModel()
        registerEvents()
    }

    private fun initViews() {
        githubUsersAdapter = GithubUsersAdapter(this, this)
        val linearLayoutManager = LinearLayoutManager(this)
        rv_github_users.apply {
            layoutManager = linearLayoutManager
            adapter = githubUsersAdapter
        }
    }

    private fun registerEvents() {
        btn_search.setOnClickListener {
            doSearch()
        }
    }

    private fun doSearch() {
        val searchKey = et_search_key.text.toString()
        if (searchKey.isNotEmpty()) {
            searchViewModel.searchUsers(searchKey)
        }
    }

    private fun observeViewModel() {
        searchViewModel.viewState.observe(this, { viewState ->
            when (viewState) {
                is SearchViewModel.ViewState.Loading -> {
                    hideContent()
                    showLoading()
                    disableSearchBtn()
                }

                is SearchViewModel.ViewState.UsersFetched -> {
                    hideLoading()
                    enableSearchBtn()
                    showGithubUsersList(viewState.githubUsers)
                }

                is SearchViewModel.ViewState.Error -> {
                    hideLoading()
                    showError(viewState.error)
                    enableSearchBtn()
                }

                is SearchViewModel.ViewState.Idle -> {
                    hideContent()
                    enableSearchBtn()
                }
            }
        })

        searchViewModel.navEvent.observe(this, { navEvent ->
            when (navEvent) {
                is ActivityNavEvent -> {
                    startActivity(navEvent.buildIntent(this))
                }
            }
        })
    }

    private fun disableSearchBtn() {
        btn_search.isEnabled = false
    }

    private fun enableSearchBtn() {
        btn_search.isEnabled = true
    }

    private fun hideContent() {
        hideGithubUsersList()
        hideError()
    }

    private fun hideError() {
        iv_alert.gone()
        tv_common_error.gone()
        iv_face.gone()
        tv_no_user_found.gone()
    }

    private fun hideGithubUsersList() {
        rv_github_users.gone()
    }

    private fun showError(error: SearchViewModel.SearchGithubUserException) {
        when (error) {
            is SearchViewModel.SearchGithubUserException.CommonException -> {
                iv_alert.visible()
                tv_common_error.visible()
            }
            is SearchViewModel.SearchGithubUserException.EmptyResultException -> {
                iv_face.visible()
                tv_no_user_found.visible()
            }
        }
    }

    private fun showGithubUsersList(githubUsers: List<GithubUser>) {
        rv_github_users.visible()
        githubUsersAdapter.setData(githubUsers)
    }

    private fun hideLoading() {
        tv_loading_description.gone()
        pb_search_users.gone()
    }

    private fun showLoading() {
        tv_loading_description.visible()
        pb_search_users.visible()
    }

    override fun onItemClick(loginName: String) {
        searchViewModel.onItemClick(loginName)
    }
}