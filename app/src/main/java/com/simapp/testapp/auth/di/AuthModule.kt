package com.simapp.testapp.auth.di

import com.simapp.testapp.auth.data.*
import com.simapp.testapp.auth.domain.AuthUseCasesImpl
import com.simapp.testapp.auth.domain.IAuthRepository
import com.simapp.testapp.auth.domain.IAuthUseCases
import com.simapp.testapp.auth.navigator.*
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

    @Binds
    @Named("GOOGLE")
    abstract fun getGoogleServerDataSource(ds: GoogleUserDataSource): IServerDataSource

    @Binds
    @Named("VK")
    abstract fun getVkNavigator(navigator: VkAuthNavigatorImpl): IAuthNetworkNavigator

    @Binds
    @Named("FB")
    abstract fun getFbNavigator(navigator: FbAuthNavigatorImpl): IAuthNetworkNavigator

    @Binds
    @Named("GOOGLE")
    abstract fun getGoogleNavigator(navigator: GoogleAuthNavigatorImpl): IAuthNetworkNavigator

}