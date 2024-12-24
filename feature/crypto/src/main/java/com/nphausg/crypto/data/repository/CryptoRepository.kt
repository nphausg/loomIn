package com.nphausg.crypto.data.repository

import com.nphausg.crypto.data.model.CryptoPriceResponse
import com.nphausg.crypto.data.network.CryptoApiService

class CryptoRepository(private val api: CryptoApiService) {

    suspend fun fetchCryptoPrices(): List<CryptoPriceResponse> {
        val prices = api.getCryptoPrices()
        return listOf(
            CryptoPriceResponse("bitcoin", "Bitcoin", "BTC", prices["bitcoin"]?.get("usd") ?: 0.0),
            CryptoPriceResponse("ethereum", "Ethereum", "ETH", prices["ethereum"]?.get("usd") ?: 0.0)
        )
    }
}