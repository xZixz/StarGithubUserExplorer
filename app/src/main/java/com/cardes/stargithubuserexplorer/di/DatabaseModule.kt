package com.cardes.stargithubuserexplorer.di

import android.content.Context
import androidx.room.Room
import com.cardes.stargithubuserexplorer.data.database.AppDb
import com.cardes.stargithubuserexplorer.data.database.AppDb.Companion.DATABASE_NAME
import com.cardes.stargithubuserexplorer.data.database.dao.GithubUserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun roomDb(context: Context): AppDb {
        return Room.databaseBuilder(context, AppDb::class.java, DATABASE_NAME)
            .build()
    }

    @Provides
    fun githubUserDao(roomDb: AppDb): GithubUserDao = roomDb.githubUserDao()
}