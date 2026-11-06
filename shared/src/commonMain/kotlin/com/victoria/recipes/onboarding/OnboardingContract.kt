package com.victoria.recipes.onboarding

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import recipesapp.shared.generated.resources.Res
import recipesapp.shared.generated.resources.img_mascot_cook
import recipesapp.shared.generated.resources.img_mascot_favorites
import recipesapp.shared.generated.resources.img_mascot_find
import recipesapp.shared.generated.resources.img_mascot_welcome
import recipesapp.shared.generated.resources.onboarding_desc_1
import recipesapp.shared.generated.resources.onboarding_desc_2
import recipesapp.shared.generated.resources.onboarding_desc_3
import recipesapp.shared.generated.resources.onboarding_desc_4
import recipesapp.shared.generated.resources.onboarding_title_1
import recipesapp.shared.generated.resources.onboarding_title_2
import recipesapp.shared.generated.resources.onboarding_title_3
import recipesapp.shared.generated.resources.onboarding_title_4

data class OnboardingPage(
    val image: DrawableResource,
    val title: StringResource,
    val description: StringResource,
)

data class OnboardingState(
    val pages: List<OnboardingPage> = defaultPages,
    val currentPage: Int = 0,
) {
    val isFirstPage: Boolean get() = currentPage == 0
    val isLastPage: Boolean get() = currentPage == pages.lastIndex
}

sealed interface OnboardingIntent {
    data class PageChanged(val page: Int) : OnboardingIntent
    data object NextClicked : OnboardingIntent
    data object BackClicked : OnboardingIntent
    data object SkipClicked : OnboardingIntent
}

sealed interface OnboardingEffect {
    data class ScrollToPage(val page: Int) : OnboardingEffect
    data object NavigateToAuth : OnboardingEffect
    data object NavigateToHome : OnboardingEffect
}

private val defaultPages = listOf(
    OnboardingPage(Res.drawable.img_mascot_welcome, Res.string.onboarding_title_1, Res.string.onboarding_desc_1),
    OnboardingPage(Res.drawable.img_mascot_find, Res.string.onboarding_title_2, Res.string.onboarding_desc_2),
    OnboardingPage(Res.drawable.img_mascot_favorites, Res.string.onboarding_title_3, Res.string.onboarding_desc_3),
    OnboardingPage(Res.drawable.img_mascot_cook, Res.string.onboarding_title_4, Res.string.onboarding_desc_4),
)
