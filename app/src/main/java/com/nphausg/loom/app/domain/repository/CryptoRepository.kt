package com.nphausg.loom.app.domain.repository

import com.nphausg.loom.app.domain.model.CryptoPrice

fun interface CryptoRepository {
    suspend fun invoke(): List<CryptoPrice>
}
