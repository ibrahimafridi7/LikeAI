package com.likeai.ecommerce.di

import com.likeai.ecommerce.data.repository.*
import com.likeai.ecommerce.data.repository.impl.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return FirebaseAuthRepository()
    }

    @Provides
    @Singleton
    fun provideProductRepository(): ProductRepository {
        return FirebaseProductRepository()
    }

    @Provides
    @Singleton
    fun provideCartRepository(): CartRepository {
        return FirebaseCartRepository()
    }

    @Provides
    @Singleton
    fun provideOrderRepository(): OrderRepository {
        return FirebaseOrderRepository()
    }
} 