package com.nphausg.crypto.domain.model

data class CryptoPrice(
    val id: String,
    val name: String,
    val symbol: String,
    val currentPrice: Double,
    val priceChangePercentage24h: Double,
    val image: String = "https://cryptologos.cc/logos/bitcoin-btc-logo.png"
)
