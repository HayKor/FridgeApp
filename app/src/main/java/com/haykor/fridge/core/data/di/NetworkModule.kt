package com.haykor.fridge.core.data.di

import com.haykor.fridge.BuildConfig
import com.haykor.fridge.core.data.local.datastore.TokenManager
import com.haykor.fridge.core.data.remote.adapters.ResultCallAdapterFactory
import com.haykor.fridge.core.data.remote.api.AuthService
import com.haykor.fridge.core.data.remote.api.FridgeProductsService
import com.haykor.fridge.core.data.remote.api.FridgesService
import com.haykor.fridge.core.data.remote.api.UserService
import com.haykor.fridge.core.data.remote.interceptors.AuthInterceptor
import com.haykor.fridge.core.data.remote.interceptors.UserAgentInterceptor
import com.haykor.fridge.core.data.remote.util.InstantSerializer
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton
import kotlin.time.ExperimentalTime


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideFridgesService(retrofit: Retrofit): FridgesService {
        return retrofit.create(FridgesService::class.java)
    }

    @Provides
    @Singleton
    fun provideFridgeProductsService(retrofit: Retrofit): FridgeProductsService {
        return retrofit.create(FridgeProductsService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
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

    @OptIn(ExperimentalTime::class)
    @Provides
    @Singleton
    fun provideJson(): Json {
        val module = SerializersModule {
            contextual(InstantSerializer)
        }
        return Json {
            ignoreUnknownKeys = true
            serializersModule = module
            // isLenient = true
        }
    }

    @Provides
    @Singleton
    fun provideResultCallAdapterFactory(): ResultCallAdapterFactory {
        return ResultCallAdapterFactory(CoroutineScope(Dispatchers.IO))
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenManager: TokenManager,
        authService: Lazy<AuthService>
    ): AuthInterceptor {
        return AuthInterceptor(tokenManager, authService)
    }

    @Provides
    @Singleton
    fun provideUserAgentInterceptor(): UserAgentInterceptor {
        return UserAgentInterceptor()
    }
}