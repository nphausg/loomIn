package com.nphausg.crypto.data.model

data class CryptoPriceResponse(
    val id: String,
    val name: String,
    val symbol: String,
    val currentPrice: Double
)