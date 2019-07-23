package com.simapp.testapp.auth.navigator

import android.support.v4.app.FragmentActivity
import com.simapp.testapp.auth.domain.AuthTypes
import io.reactivex.Maybe
import javax.inject.Inject

class AuthNavigatorImpl @Inject constructor(): IAuthNavigator {
    override fun runAuth(activity: FragmentActivity, type: AuthTypes): Maybe<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}