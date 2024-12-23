package com.nphausg.app.foundation.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource

@Stable
sealed interface UiText {
    class Str(val str: String = "") : UiText
    class RedId(@StringRes val resId: Int) : UiText
}

val String?.asUiText: UiText
    get() = UiText.Str(this.orEmpty())

val Int.asUiText: UiText
    get() = UiText.RedId(this)

val UiText.value: String
    @Composable
    get() = when (this) {
        is UiText.Str -> this.str
        is UiText.RedId -> stringResource(this.resId)
    }