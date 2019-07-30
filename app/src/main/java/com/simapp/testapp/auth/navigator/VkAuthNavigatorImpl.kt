package com.simapp.testapp.auth.navigator

import android.app.Activity
import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.simapp.testapp.main.IRxActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import io.reactivex.Maybe
import javax.inject.Inject


class VkAuthNavigatorImpl @Inject constructor(): IAuthNetworkNavigator {
    override fun runAuth(activity: FragmentActivity): Maybe<String> {
        val splitPermissions = ""
        val vkScopeList = VKScope.values()
                .filter { splitPermissions.contains(it.name.toLowerCase()) }

        VK.login(activity, vkScopeList)
        if (activity !is IRxActivity) {
            return Maybe.empty()

        }
        return getResult(activity)
    }

    override fun getResult(activity: FragmentActivity): Maybe<String> {
        if (activity !is IRxActivity) return Maybe.empty()
        return activity
                .rxActivityResult
                .filter { it.resultCode == Activity.RESULT_OK }
                .firstElement()
                .flatMap { result ->
                    Maybe.create<String> { emitter ->
                        val intentData = result.intent ?: Intent()
                        VK.onActivityResult(result.requestCode, result.resultCode, intentData, object : VKAuthCallback {
                            override fun onLogin(token: VKAccessToken) {
                                if (token.isValid) {
                                    emitter.onSuccess(token.accessToken)
                                }
                            }

                            override fun onLoginFailed(errorCode: Int) {
                                //do nothing, let user choose another option
                            }
                        })
                    }
                }
    }

}