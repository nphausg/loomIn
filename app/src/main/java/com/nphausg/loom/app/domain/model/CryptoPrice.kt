package com.nphausg.loom.app.domain.model

data class CryptoPrice(
    val id: String,
    val name: String,
    val image: String,
    val symbol: String,
    val currentPrice: Double,
    val priceChangePercentage24h: Double
)
