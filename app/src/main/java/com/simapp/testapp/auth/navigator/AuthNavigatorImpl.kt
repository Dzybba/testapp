package com.simapp.testapp.auth.navigator

import android.support.v4.app.FragmentActivity
import com.simapp.testapp.auth.domain.AuthTypes
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Named


class AuthNavigatorImpl @Inject constructor(
        @Named("GOOGLE")
        private val googleNavigator: IAuthNetworkNavigator,
        @Named("FB")
        private val fbNavigator: IAuthNetworkNavigator,
        @Named("VK")
        private val vkNavigator: IAuthNetworkNavigator
) : IAuthNavigator {

    override fun runAuth(activity: FragmentActivity, type: AuthTypes): Maybe<String> {
        return getNavigator(type).runAuth(activity)
    }

    override fun getResult(activity: FragmentActivity, type: AuthTypes): Maybe<String> {
        return getNavigator(type).getResult(activity)
    }

    private fun getNavigator(type: AuthTypes): IAuthNetworkNavigator {
        return when(type) {
            AuthTypes.VK -> vkNavigator
            AuthTypes.FB -> fbNavigator
            AuthTypes.GOOGLE -> googleNavigator
        }
    }

}