package com.simapp.testapp.auth.di

import com.simapp.testapp.auth.data.AuthRepositoryImpl
import com.simapp.testapp.auth.data.FbServerDataSource
import com.simapp.testapp.auth.data.IServerDataSource
import com.simapp.testapp.auth.data.VkServerDataSource
import com.simapp.testapp.auth.domain.AuthUseCasesImpl
import com.simapp.testapp.auth.domain.IAuthRepository
import com.simapp.testapp.auth.domain.IAuthUseCases
import com.simapp.testapp.auth.navigator.AuthNavigatorImpl
import com.simapp.testapp.auth.navigator.IAuthNavigator
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module
abstract class AuthModule {
    @Binds
    abstract fun getAuthUseCases(useCases: AuthUseCasesImpl): IAuthUseCases
    @Binds
    abstract fun getNavigator(navigatorImpl: AuthNavigatorImpl): IAuthNavigator

    @Binds
    abstract fun repository(rep: AuthRepositoryImpl): IAuthRepository

    @Binds
    @Named("VK")
    abstract fun getVkServerDataSource(ds: VkServerDataSource): IServerDataSource

    @Binds
    @Named("FB")
    abstract fun getFBServerDataSource(ds: FbServerDataSource): IServerDataSource
}