package com.cardes.stargithubuserexplorer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cardes.stargithubuserexplorer.domain.model.GithubUser

class UiTestUtil {
    companion object {
        fun createViewModelFor(viewModel: ViewModel): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {

                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(viewModel.javaClass)) {
                        return viewModel as T
                    }
                    throw IllegalArgumentException("unexpected model class $modelClass")
                }

            }
        }

        fun generateGithubUser(
            loginName: String = "superman",
            name: String = "Clark Kent",
            avatarUrl: String = "https://superman_with_cape_png",
            location: String = "Metropolis",
            email: String = "super.man@awesome.com",
            followersNum: Int = 1_333_333,
            blogUrl: String = "https://www.superman.awesome",
            company: String = "The Planet"
        ): GithubUser {
            return GithubUser(
                loginName = loginName,
                name = name,
                avatarUrl = avatarUrl,
                location = location,
                email = email,
                followersNum = followersNum,
                blogUrl = blogUrl,
                company = company
            )
        }
    }
}
