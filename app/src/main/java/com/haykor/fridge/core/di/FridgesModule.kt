package com.haykor.fridge.core.di

import com.haykor.fridge.core.data.remote.api.FridgeProductsService
import com.haykor.fridge.core.data.remote.api.FridgesService
import com.haykor.fridge.feature.home.data.FridgeProductsRepository
import com.haykor.fridge.feature.home.data.FridgeProductsRepositoryImpl
import com.haykor.fridge.feature.home.data.FridgesRepository
import com.haykor.fridge.feature.home.data.FridgesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FridgesModule {

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
    fun provideFridgesRepository(
        fridgesService: FridgesService
    ): FridgesRepository {
        return FridgesRepositoryImpl(fridgesService)
    }

    @Provides
    @Singleton
    fun provideFridgeProductsRepository(
        fridgeProductsService: FridgeProductsService
    ): FridgeProductsRepository {
        return FridgeProductsRepositoryImpl(fridgeProductsService)
    }
}