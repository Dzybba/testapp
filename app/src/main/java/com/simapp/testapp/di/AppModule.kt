package com.simapp.testapp.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created by denisdobryshkin
 * on 29.01.2018.

 * This is a Dagger module. We use this to bind our Application class as a Context in the AppComponent
 * By using Dagger Android we do not need to pass our Application instance to any module,
 * we simply need to expose our Application as Context.
 * One of the advantages of Dagger.Android is that your
 * Application & Activities are provided into your graph for you.
 * {@link
 * AppComponent}.
 */

@Module
abstract class AppModule {
    @Singleton
    @Binds
    abstract fun appContext(application: Application): Context
}