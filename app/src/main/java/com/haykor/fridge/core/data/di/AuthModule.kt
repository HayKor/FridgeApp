package com.haykor.fridge.core.data.di

import com.haykor.fridge.feature.auth.data.AuthRepository
import com.haykor.fridge.feature.auth.data.AuthRepositoryImpl
import com.haykor.fridge.core.data.local.datastore.TokenManager
import com.haykor.fridge.core.data.remote.api.AuthService
import com.haykor.fridge.core.data.remote.api.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

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