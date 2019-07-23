package com.simapp.testapp.auth.di

import com.simapp.testapp.auth.domain.AuthUseCasesImpl
import com.simapp.testapp.auth.domain.IAuthUseCases
import dagger.Binds
import dagger.Module

@Module
abstract class AuthModule {
    @Binds
    abstract fun getAuthUseCases(useCases: AuthUseCasesImpl): IAuthUseCases
}