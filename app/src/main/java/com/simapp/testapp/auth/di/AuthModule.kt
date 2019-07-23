package com.simapp.testapp.auth.di

import com.simapp.testapp.auth.domain.AuthUseCasesImpl
import com.simapp.testapp.auth.domain.IAuthUseCases
import com.simapp.testapp.auth.navigator.AuthNavigatorImpl
import com.simapp.testapp.auth.navigator.IAuthNavigator
import dagger.Binds
import dagger.Module

@Module
abstract class AuthModule {
    @Binds
    abstract fun getAuthUseCases(useCases: AuthUseCasesImpl): IAuthUseCases
    @Binds
    abstract fun getNavigator(navigatorImpl: AuthNavigatorImpl): IAuthNavigator
}