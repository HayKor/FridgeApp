package com.haykor.fridge.core.di

import com.haykor.fridge.core.data.remote.api.ProductTypesService
import com.haykor.fridge.core.data.remote.api.ProductsService
import com.haykor.fridge.feature.products.data.ProductsRepository
import com.haykor.fridge.feature.products.data.ProductsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductsModule {

    @Provides
    @Singleton
    fun provideProductsRepository(
        productsService: ProductsService,
        productTypesService: ProductTypesService
    ): ProductsRepository {
        return ProductsRepositoryImpl(productsService, productTypesService)
    }

    @Provides
    @Singleton
    fun provideProductsService(retrofit: Retrofit): ProductsService {
        return retrofit.create(ProductsService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductTypesService(retrofit: Retrofit): ProductTypesService {
        return retrofit.create(ProductTypesService::class.java)
    }
}