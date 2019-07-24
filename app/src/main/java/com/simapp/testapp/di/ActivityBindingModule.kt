package com.simapp.testapp.di

import com.simapp.testapp.auth.presentation.AuthFragmentModule
import com.simapp.testapp.github.presentation.GitHubSearchFragmentModule
import com.simapp.testapp.ui.main.MainActivity
import com.simapp.testapp.ui.main.MainFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by denisdobryshkin
 * on 29.01.2018.
 */

@Module
abstract class ActivityBindingModule {
    @Suppress("unused")
    @ActivityScoped
    @ContributesAndroidInjector(modules = [
        AuthFragmentModule::class,
        GitHubSearchFragmentModule::class,
        MainFragmentModule::class
    ])
    abstract fun mainActivity(): MainActivity
}
