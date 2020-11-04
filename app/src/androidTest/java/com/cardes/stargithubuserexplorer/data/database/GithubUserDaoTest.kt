package com.cardes.stargithubuserexplorer.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cardes.stargithubuserexplorer.data.database.dao.GithubUserDao
import com.cardes.stargithubuserexplorer.data.database.entity.GithubUserEntity
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class GithubUserDaoTest {

    private lateinit var db: AppDb
    private lateinit var githubUserDao: GithubUserDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDb::class.java)
            .build()
        githubUserDao = db.githubUserDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetGithubUsers() {
        val superman = generateGithubUserEntity()
        val batman = generateGithubUserEntity(
            loginName = "batman",
            name = "Bruce Wayne",
            avatarUrl = "https://image.com/batman_with_bat_mobile.png",
            location = "Gotham",
            email = "bat.man@awesome.com",
            followersNum = 50
        )
        githubUserDao.insert(superman, batman)
        val searchSuperman = githubUserDao.getUser("superman")
            .blockingGet()
        assertThat(superman, equalTo(searchSuperman))
        val searchBatman = githubUserDao.getUser("batman")
            .blockingGet()
        assertThat(batman, equalTo(searchBatman))
    }

    private fun generateGithubUserEntity(
        loginName: String = "superman",
        name: String = "Clark Kent",
        avatarUrl: String = "https://image.com/superman_with_cape_png",
        location: String = "Metropolis",
        email: String = "super.man@awesome.com",
        followersNum: Int = 1_333_333,
        bio: String = "Strong but no money",
        company: String = "The Planet",
        blogUrl: String = "https://superman.blog"
    ): GithubUserEntity {
        return GithubUserEntity(
            loginName = loginName,
            name = name,
            avatarUrl = avatarUrl,
            location = location,
            email = email,
            followersNum = followersNum,
            bio = bio,
            company = company,
            blogUrl = blogUrl
        )
    }
}