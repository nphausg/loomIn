package com.nphausg.crypto.data.repository

import com.nphausg.crypto.data.model.CryptoPriceResponse
import com.nphausg.crypto.data.network.CryptoApiService
import com.nphausg.crypto.domain.model.CryptoPrice
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class CryptoRepository @Inject constructor(
    // private val apiService: CryptoApiService
) {

    val cache: MutableList<CryptoPriceResponse> = mutableListOf()

    object RetrofitInstance {
        private const val BASE_URL = "https://api.coingecko.com/api/"
        val api: CryptoApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CryptoApiService::class.java)
        }
    }

    suspend fun fetchCryptoPrices(): List<CryptoPrice> {
        if (cache.isEmpty()) {
            cache.addAll(RetrofitInstance.api.getCryptoPrices())
        }
        return cache.map {
            CryptoPrice(
                id = it.id,
                name = it.name,
                image = it.image,
                symbol = it.symbol,
                currentPrice = it.currentPrice,
                priceChangePercentage24h = it.priceChangePercentage24h
            )
        }
    }
}
