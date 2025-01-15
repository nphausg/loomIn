package com.nphausg.crypto.data.repository

import com.nphausg.crypto.data.model.CryptoPriceResponse
import com.nphausg.crypto.data.network.CryptoApiService
import com.nphausg.crypto.domain.model.CryptoPrice
import javax.inject.Inject

class CryptoRepository @Inject constructor(
    private val apiService: CryptoApiService
) {

    private val cache: MutableList<CryptoPriceResponse> = mutableListOf()

    suspend fun fetchCryptoPrices(): List<CryptoPrice> {
        if (cache.isEmpty()) {
            cache.addAll(apiService.getCryptoPrices())
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
