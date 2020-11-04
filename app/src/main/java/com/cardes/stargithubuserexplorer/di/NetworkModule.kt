package com.cardes.stargithubuserexplorer.di

import com.cardes.stargithubuserexplorer.data.api.GithubApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(generateClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory((RxJava2CallAdapterFactory.create()))
            .build()
    }

    @Provides
    fun githubApi(retrofit: Retrofit): GithubApi {
        return retrofit.create(GithubApi::class.java)
    }

    private fun generateClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .followRedirects(true)
            .retryOnConnectionFailure(true)
            .addInterceptor(loggingInterceptor)
            .cache(null)
            .build()
    }

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
}