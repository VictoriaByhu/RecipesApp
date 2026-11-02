package com.victoria.recipes.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.intl.Locale

/** Глобальний об'єкт браузера; __customLocale читає скрипт в index.html. */
external object window {
    var __customLocale: String?
}

actual object LocalAppLocale {
    private val LocalAppLocaleTag = staticCompositionLocalOf { Locale.current.toLanguageTag() }

    actual val current: String
        @Composable get() = LocalAppLocaleTag.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        window.__customLocale = value?.replace('_', '-')
        return LocalAppLocaleTag.provides(Locale.current.toLanguageTag())
    }
}
