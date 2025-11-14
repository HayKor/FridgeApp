package com.haykor.fridge.core.data.di

import com.haykor.fridge.BuildConfig
import com.haykor.fridge.auth.data.AuthRepository
import com.haykor.fridge.auth.data.AuthRepositoryImpl
import com.haykor.fridge.core.data.local.datastore.TokenManager
import com.haykor.fridge.core.data.remote.adapters.ResultCallAdapterFactory
import com.haykor.fridge.core.data.remote.api.AuthService
import com.haykor.fridge.core.data.remote.interceptors.AuthInterceptor
import com.haykor.fridge.core.data.remote.interceptors.UserAgentInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        userAgentInterceptor: UserAgentInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(userAgentInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
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
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            // isLenient = true
        }
    }

    @Provides
    @Singleton
    fun provideResultCallAdapterFactory(): ResultCallAdapterFactory {
        return ResultCallAdapterFactory()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenManager: TokenManager,
        authService: AuthService
    ): AuthInterceptor {
        return AuthInterceptor(tokenManager, authService)
    }

    @Provides
    @Singleton
    fun provideUserAgentInterceptor(): UserAgentInterceptor {
        return UserAgentInterceptor()
    }
}