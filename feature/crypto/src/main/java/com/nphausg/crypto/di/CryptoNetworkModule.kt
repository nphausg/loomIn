package com.nphausg.crypto.di

import com.nphausg.crypto.data.network.CryptoApiService
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