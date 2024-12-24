package com.nphausg.crypto.data.network

import retrofit2.http.GET

interface CryptoApiService {
    @GET("simple/price?ids=bitcoin,ethereum&vs_currencies=usd")
    suspend fun getCryptoPrices(): Map<String, Map<String, Double>>
}