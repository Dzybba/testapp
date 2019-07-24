package com.simapp.testapp.auth.navigator

import android.app.Activity
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.simapp.testapp.ui.main.IRxActivity
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
                                Log.e("DD", "onLogin token ${token.isValid} ${token.accessToken}")
                                if (token.isValid) {
                                    emitter.onSuccess(token.accessToken)
                                }
                            }

                            override fun onLoginFailed(errorCode: Int) {
                                Log.e("DD", "onLogin failed")
                                //do nothing, let user choose another option
                            }
                        })
                    }
                }
    }

}