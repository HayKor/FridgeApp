package com.haykor.fridge.core.di

import com.haykor.fridge.BuildConfig
import com.haykor.fridge.core.data.remote.adapters.ResultCallAdapterFactory
import com.haykor.fridge.core.data.remote.api.RefreshTokensService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ForRefreshAuth

@Module
@InstallIn(SingletonComponent::class)
object RefreshAuthModule {

    @Provides
    @Singleton
    fun provideRefreshTokensService(
        @ForRefreshAuth retrofit: Retrofit
    ): RefreshTokensService {
        return retrofit.create(RefreshTokensService::class.java)
    }

    @Provides
    @Singleton
    @ForRefreshAuth
    fun provideRetrofit(
        json: Json,
        @ForRefreshAuth okHttpClient: OkHttpClient,
        resultCallAdapterFactory: ResultCallAdapterFactory
    ): Retrofit {
        val jsonConverter = json.asConverterFactory(
            contentType = "application/json; charset=UTF8".toMediaType()
        )
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverter)
            .addCallAdapterFactory(resultCallAdapterFactory)
            .build()
    }

    @Provides
    @Singleton
    @ForRefreshAuth
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }
}
