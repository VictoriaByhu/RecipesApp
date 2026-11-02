package com.victoria.recipes.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale

actual object LocalAppLocale {
    private var default: Locale? = null
    private val LocalAppLocaleTag = staticCompositionLocalOf { Locale.getDefault().toString() }

    actual val current: String
        @Composable get() = LocalAppLocaleTag.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        if (default == null) default = Locale.getDefault()
        val newLocale = if (value == null) default!! else Locale.forLanguageTag(value)
        Locale.setDefault(newLocale)
        return LocalAppLocaleTag.provides(newLocale.toString())
    }
}
