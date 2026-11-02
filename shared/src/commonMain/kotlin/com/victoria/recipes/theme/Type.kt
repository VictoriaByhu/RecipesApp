package com.victoria.recipes.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import recipesapp.shared.generated.resources.Res
import recipesapp.shared.generated.resources.montserrat_bold
import recipesapp.shared.generated.resources.montserrat_extrabold
import recipesapp.shared.generated.resources.montserrat_medium
import recipesapp.shared.generated.resources.montserrat_regular
import recipesapp.shared.generated.resources.montserrat_semibold

@Composable
fun montserratFamily(): FontFamily = FontFamily(
    Font(Res.font.montserrat_regular, FontWeight.Normal),
    Font(Res.font.montserrat_medium, FontWeight.Medium),
    Font(Res.font.montserrat_semibold, FontWeight.SemiBold),
    Font(Res.font.montserrat_bold, FontWeight.Bold),
    Font(Res.font.montserrat_extrabold, FontWeight.ExtraBold),
)

/** Типографіка Material 3, у якій усі стилі використовують шрифт Montserrat. */
@Composable
fun appTypography(): Typography {
    val family = montserratFamily()
    val base = Typography()
    return Typography(
        displayLarge = base.displayLarge.copy(fontFamily = family, fontWeight = FontWeight.ExtraBold),
        displayMedium = base.displayMedium.copy(fontFamily = family, fontWeight = FontWeight.ExtraBold),
        displaySmall = base.displaySmall.copy(fontFamily = family, fontWeight = FontWeight.ExtraBold),
        headlineLarge = base.headlineLarge.copy(fontFamily = family, fontWeight = FontWeight.ExtraBold),
        headlineMedium = base.headlineMedium.copy(fontFamily = family, fontWeight = FontWeight.ExtraBold),
        headlineSmall = base.headlineSmall.copy(fontFamily = family, fontWeight = FontWeight.ExtraBold),
        titleLarge = base.titleLarge.copy(fontFamily = family, fontWeight = FontWeight.SemiBold),
        titleMedium = base.titleMedium.copy(fontFamily = family, fontWeight = FontWeight.SemiBold),
        titleSmall = base.titleSmall.copy(fontFamily = family, fontWeight = FontWeight.Medium),
        bodyLarge = base.bodyLarge.copy(fontFamily = family),
        bodyMedium = base.bodyMedium.copy(fontFamily = family),
        bodySmall = base.bodySmall.copy(fontFamily = family),
        labelLarge = base.labelLarge.copy(fontFamily = family, fontWeight = FontWeight.SemiBold),
        labelMedium = base.labelMedium.copy(fontFamily = family, fontWeight = FontWeight.Medium),
        labelSmall = base.labelSmall.copy(fontFamily = family, fontWeight = FontWeight.Medium),
    )
}
