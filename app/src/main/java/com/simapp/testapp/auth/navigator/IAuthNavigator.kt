package com.simapp.testapp.auth.navigator

import android.support.v4.app.FragmentActivity
import com.simapp.testapp.auth.domain.AuthTypes
import io.reactivex.Maybe

interface IAuthNavigator {
    fun runAuth(activity: FragmentActivity, type: AuthTypes): Maybe<String>
}