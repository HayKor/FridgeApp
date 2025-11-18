package com.haykor.fridge.core.data.di

import com.haykor.fridge.auth.data.AuthRepository
import com.haykor.fridge.auth.data.AuthRepositoryImpl
import com.haykor.fridge.core.data.local.datastore.TokenManager
import com.haykor.fridge.core.data.remote.api.AuthService
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
        tokenManager: TokenManager
    ): AuthRepository {
        return AuthRepositoryImpl(
            authService = authService,
            tokenManager = tokenManager
        )
    }
}