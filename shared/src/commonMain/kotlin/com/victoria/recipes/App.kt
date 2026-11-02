package com.victoria.recipes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.rememberNavBackStack
import co.touchlab.kermit.Logger
import com.victoria.recipes.locale.AppLocaleEnvironment
import com.victoria.recipes.main.MainViewModel
import com.victoria.recipes.navigation.AppNavigation
import com.victoria.recipes.navigation.navigationConfig
import com.victoria.recipes.theme.RecipesTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(viewModel: MainViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val backStack = rememberNavBackStack(navigationConfig, viewModel.startRoute)

    LaunchedEffect(Unit) {
        Logger.setTag("RecipesApp")
        Logger.i { "App started on ${getPlatform().name}" }
    }

    AppLocaleEnvironment(languageTag = state.languageTag) {
        RecipesTheme(themeMode = state.themeMode) {
            AppNavigation(backStack = backStack)
        }
    }
}
