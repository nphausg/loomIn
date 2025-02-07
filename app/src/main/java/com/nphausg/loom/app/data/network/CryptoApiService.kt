package com.nphausg.loom.app.data.network

import com.nphausg.loom.app.data.model.CryptoPriceResponse
import retrofit2.http.GET

interface CryptoApiService {
    @GET("v3/coins/markets?vs_currency=usd")
    suspend fun getCryptoPrices(): List<CryptoPriceResponse>
}