package com.cardes.stargithubuserexplorer.components.githubuserdetail

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.text.util.Linkify
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cardes.stargithubuserexplorer.R
import com.cardes.stargithubuserexplorer.common.StringUtil
import com.cardes.stargithubuserexplorer.common.gone
import com.cardes.stargithubuserexplorer.common.visible
import com.cardes.stargithubuserexplorer.components.ViewModelFactory
import com.cardes.stargithubuserexplorer.domain.model.GithubUser
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_github_user_detail.*
import javax.inject.Inject

class GithubUserDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var githubUserDetailViewModel: GithubUserDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_user_detail)

        githubUserDetailViewModel = ViewModelProvider(this, viewModelFactory)
            .get(GithubUserDetailViewModel::class.java)

        observeViewModel()

        val loginName = intent.getStringExtra(LOGIN_NAME_KEY).orEmpty()
        githubUserDetailViewModel.init(loginName)

        btn_retry.setOnClickListener {
            githubUserDetailViewModel.getUser()
        }
    }

    private fun observeViewModel() {
        githubUserDetailViewModel.viewState.observe(this, { viewState ->
            when (viewState) {
                is GithubUserDetailViewModel.ViewState.Loading -> {
                    hideContent()
                    showLoading()
                }

                is GithubUserDetailViewModel.ViewState.Success -> {
                    hideLoading()
                    showUserDetail(viewState.githubUser)
                }

                is GithubUserDetailViewModel.ViewState.Error -> {
                    hideLoading()
                    showError()
                }

                is GithubUserDetailViewModel.ViewState.Idle -> {
                    hideContent()
                }
            }
        })
    }

    private fun showLoading() {
        pb_load_user.visible()
    }

    private fun hideLoading() {
        pb_load_user.gone()
    }

    private fun showError() {
        container_error.visible()
    }

    private fun hideError() {
        container_error.gone()
    }

    private fun hideContent() {
        hideError()
        hideUserDetail()
    }

    private fun hideUserDetail() {
        container_user_detail.gone()
    }

    private fun showUserDetail(githubUser: GithubUser) {
        container_user_detail.visible()
        githubUser.run {
            Glide.with(this@GithubUserDetailActivity)
                .load(avatarUrl)
                .into(iv_user_avatar)
            tv_login_name.text = loginName
            tv_user_name.text = name
            tv_followers_num.text = buildFollowersNumString(followersNum)

            if (location.isNotEmpty()) {
                iv_location.visible()
                tv_location.visible()
                tv_location.text = location
            }

            if (company.isNotEmpty()) {
                iv_company.visible()
                tv_company.visible()
                tv_company.text = company
            }

            if (blogUrl.isNotEmpty()) {
                iv_link.visible()
                tv_blog_url.visible()
                tv_blog_url.text = blogUrl
                Linkify.addLinks(tv_blog_url, Linkify.WEB_URLS)
            }
        }
    }

    private fun buildFollowersNumString(followersNum: Int): Spannable {
        val followersNumCompactString = StringUtil.formatCompactNum(followersNum.toLong())
        val followersNumString = resources.getQuantityString(
            R.plurals.followers_num,
            followersNum,
            followersNumCompactString
        )
        val spannableStringBuilder = SpannableStringBuilder(followersNumString)
        spannableStringBuilder.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            followersNumCompactString.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        return spannableStringBuilder
    }

    companion object {
        const val LOGIN_NAME_KEY = "login_name_key"
    }
}