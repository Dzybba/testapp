package com.simapp.testapp.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.simapp.testapp.R
import com.simapp.testapp.auth.domain.AuthTypes
import com.simapp.testapp.auth.domain.IAuthUseCases
import com.simapp.testapp.auth.navigator.IAuthNavigator
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class MainActivity: AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var authUseCases: IAuthUseCases

    @Inject
    lateinit var authNavigator: IAuthNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        authNavigator.runAuth(this, AuthTypes.VK)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }
}
