package com.simapp.testapp.auth.navigator

import android.support.v4.app.FragmentActivity
import com.simapp.testapp.auth.domain.AuthTypes
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import io.reactivex.Maybe
import javax.inject.Inject

class AuthNavigatorImpl @Inject constructor(): IAuthNavigator {
    override fun runAuth(activity: FragmentActivity, type: AuthTypes): Maybe<String> {
        val splitPermissions = ""
        val vkScopeList = VKScope.values()
                .filter { splitPermissions.contains(it.name.toLowerCase()) }

        VK.login(activity, vkScopeList)
        return Maybe.empty()
    }
}