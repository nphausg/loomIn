package com.nphausg.loom.app.domain.usecase

fun interface GetRemoteConfigUseCase {
    operator fun invoke(): String
}