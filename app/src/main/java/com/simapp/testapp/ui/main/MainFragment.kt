package com.simapp.testapp.ui.main

import com.simapp.clean.base.presentation.DaggerBaseCleanFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentModule {
    @ContributesAndroidInjector
    abstract fun getFragment(): MainFragment
}

class MainFragment: DaggerBaseCleanFragment<IContract.IMainView, MainFragmentPresenter>(), IContract.IMainView {

}