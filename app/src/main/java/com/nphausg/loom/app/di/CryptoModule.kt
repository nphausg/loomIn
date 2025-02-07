package com.nphausg.loom.app.di

import com.nphausg.loom.app.data.CryptoRepositoryImpl
import com.nphausg.loom.app.data.network.CryptoApiService
import com.nphausg.loom.app.domain.repository.CryptoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CryptoNetworkModule {

    @Provides
    @Singleton
    fun provideCryptoApiService(retrofit: Retrofit): CryptoApiService =
        retrofit.create(CryptoApiService::class.java)
}


@Module
@InstallIn(SingletonComponent::class)
interface CryptoModule {
    @Binds
    @Singleton
    fun bindsCryptoRepository(retrofit: CryptoRepositoryImpl): CryptoRepository
}