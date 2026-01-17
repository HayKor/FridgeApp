package com.haykor.fridge.core.di

import com.haykor.fridge.core.data.local.datastore.TokenManager
import com.haykor.fridge.core.data.remote.api.AuthService
import com.haykor.fridge.core.data.remote.api.UserService
import com.haykor.fridge.feature.auth.data.AuthRepository
import com.haykor.fridge.feature.auth.data.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authService: AuthService,
        userService: UserService,
        tokenManager: TokenManager
    ): AuthRepository {
        return AuthRepositoryImpl(
            authService = authService,
            userService = userService,
            tokenManager = tokenManager
        )
    }
}