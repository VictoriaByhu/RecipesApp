package com.victoria.recipes.di

import com.russhwolf.settings.Settings
import com.victoria.recipes.auth.AuthViewModel
import com.victoria.recipes.data.repository.OnboardingRepositoryImpl
import com.victoria.recipes.data.repository.PreferencesRepositoryImpl
import com.victoria.recipes.data.repository.SessionRepositoryImpl
import com.victoria.recipes.domain.repository.OnboardingRepository
import com.victoria.recipes.domain.repository.PreferencesRepository
import com.victoria.recipes.domain.repository.SessionRepository
import com.victoria.recipes.domain.usecase.CompleteOnboardingUseCase
import com.victoria.recipes.domain.usecase.IsOnboardingCompletedUseCase
import com.victoria.recipes.domain.usecase.IsSignedInUseCase
import com.victoria.recipes.domain.usecase.ObserveLanguageUseCase
import com.victoria.recipes.domain.usecase.ObserveThemeModeUseCase
import com.victoria.recipes.domain.usecase.ResetOnboardingUseCase
import com.victoria.recipes.domain.usecase.SetLanguageUseCase
import com.victoria.recipes.domain.usecase.SetThemeModeUseCase
import com.victoria.recipes.domain.usecase.SignInUseCase
import com.victoria.recipes.domain.usecase.SignOutUseCase
import com.victoria.recipes.domain.usecase.ValidatePasswordUseCase
import com.victoria.recipes.domain.usecase.ValidateUsernameUseCase
import com.victoria.recipes.home.HomeViewModel
import com.victoria.recipes.main.MainViewModel
import com.victoria.recipes.onboarding.OnboardingViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single<Settings> { Settings() }
    singleOf(::OnboardingRepositoryImpl) bind OnboardingRepository::class
    singleOf(::PreferencesRepositoryImpl) bind PreferencesRepository::class
    singleOf(::SessionRepositoryImpl) bind SessionRepository::class
}

val domainModule = module {
    factoryOf(::IsOnboardingCompletedUseCase)
    factoryOf(::CompleteOnboardingUseCase)
    factoryOf(::ResetOnboardingUseCase)
    factoryOf(::ObserveThemeModeUseCase)
    factoryOf(::SetThemeModeUseCase)
    factoryOf(::ObserveLanguageUseCase)
    factoryOf(::SetLanguageUseCase)
    factoryOf(::ValidateUsernameUseCase)
    factoryOf(::ValidatePasswordUseCase)
    factoryOf(::IsSignedInUseCase)
    factoryOf(::SignInUseCase)
    factoryOf(::SignOutUseCase)
}

val presentationModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(dataModule, domainModule, presentationModule)
    }
}
