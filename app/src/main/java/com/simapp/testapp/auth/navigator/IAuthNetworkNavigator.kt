package com.simapp.testapp.auth.navigator

import android.support.v4.app.FragmentActivity
import io.reactivex.Maybe

interface IAuthNetworkNavigator {
    fun runAuth(activity: FragmentActivity): Maybe<String>
    fun getResult(activity: FragmentActivity): Maybe<String>
}