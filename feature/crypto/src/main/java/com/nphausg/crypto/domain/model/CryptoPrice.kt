package com.nphausg.crypto.domain.model

data class CryptoPrice(
    val id: String,
    val name: String,
    val symbol: String,
    val currentPrice: Double
)
