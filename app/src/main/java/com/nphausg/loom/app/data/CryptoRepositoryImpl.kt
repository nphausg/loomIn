package com.nphausg.loom.app.data

import com.nphausg.loom.app.data.model.CryptoPriceResponse
import com.nphausg.loom.app.data.network.CryptoApiService
import com.nphausg.loom.app.domain.model.CryptoPrice
import com.nphausg.loom.app.domain.repository.CryptoRepository
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(
    private val apiService: CryptoApiService
) : CryptoRepository {

    private val cache: MutableList<CryptoPriceResponse> = mutableListOf()

    override suspend fun invoke(): List<CryptoPrice> {
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