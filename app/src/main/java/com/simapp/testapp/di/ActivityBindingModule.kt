package com.simapp.testapp.di

import com.simapp.testapp.ui.main.MainActivity
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
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity
}
