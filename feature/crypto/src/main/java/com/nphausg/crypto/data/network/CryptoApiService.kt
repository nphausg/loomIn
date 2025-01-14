package com.nphausg.crypto.data.network

import com.nphausg.crypto.data.model.CryptoPriceResponse
import retrofit2.http.GET

interface CryptoApiService {
    @GET("v3/coins/markets?vs_currency=usd")
    // @GET("v3/coins/markets?ids=bitcoin,ethereum&vs_currencies=usd")
    suspend fun getCryptoPrices(): List<CryptoPriceResponse>
}