package com.haykor.fridge.core.data.di

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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

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
