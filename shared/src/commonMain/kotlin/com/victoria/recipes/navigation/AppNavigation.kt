package com.victoria.recipes.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import co.touchlab.kermit.Logger
import com.victoria.recipes.auth.AuthScreen
import com.victoria.recipes.home.HomeScreen
import com.victoria.recipes.onboarding.OnboardingScreen

private val log = Logger.withTag("Navigation")

@Composable
fun AppNavigation(backStack: NavBackStack<NavKey>) {
    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Route.Onboarding> {
                OnboardingScreen(
                    onNavigateToAuth = { backStack.navigateClearingStack(Route.Auth) },
                    onNavigateToHome = { backStack.navigateClearingStack(Route.Home) },
                )
            }
            entry<Route.Auth> {
                AuthScreen(onSignedIn = { backStack.navigateClearingStack(Route.Home) })
            }
            entry<Route.Home> {
                HomeScreen(
                    onNavigateToOnboarding = { backStack.navigateClearingStack(Route.Onboarding) },
                    onSignedOut = { backStack.navigateClearingStack(Route.Auth) },
                )
            }
        },
    )
}

private fun NavBackStack<NavKey>.navigateClearingStack(route: Route) {
    log.i { "Navigate to $route" }
    clear()
    add(route)
}
