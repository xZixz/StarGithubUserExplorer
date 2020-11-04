package com.cardes.stargithubuserexplorer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cardes.stargithubuserexplorer.data.database.AppDb.Companion.VERSION
import com.cardes.stargithubuserexplorer.data.database.dao.GithubUserDao
import com.cardes.stargithubuserexplorer.data.database.entity.GithubUserEntity

@Database(
    entities = [GithubUserEntity::class],
    version = VERSION
)
abstract class AppDb : RoomDatabase() {

    abstract fun githubUserDao(): GithubUserDao

    companion object {
        const val VERSION = 1
        const val DATABASE_NAME = "star_github_user_explorer"
    }
}