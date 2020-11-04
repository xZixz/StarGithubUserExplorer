package com.cardes.stargithubuserexplorer.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cardes.stargithubuserexplorer.data.database.entity.GithubUserEntity
import io.reactivex.Single

@Dao
interface GithubUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg githubUserEntity: GithubUserEntity)

    @Query("SELECT * FROM github_users WHERE loginName = :loginName")
    fun getUser(loginName: String): Single<GithubUserEntity>

    @Query("DELETE FROM github_users")
    fun removeAll()
}