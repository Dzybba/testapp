package com.simapp.testapp.auth.navigator

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.simapp.testapp.auth.domain.AuthTypes
import com.simapp.testapp.ui.main.IRxActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Maybe
import javax.inject.Inject

class AuthNavigatorImpl @Inject constructor() : IAuthNavigator {
    override fun runAuth(activity: FragmentActivity, type: AuthTypes): Maybe<String> {
        val splitPermissions = ""
        val vkScopeList = VKScope.values()
                .filter { splitPermissions.contains(it.name.toLowerCase()) }

        VK.login(activity, vkScopeList)
        return getResult(activity, type)
    }

    private fun getResult(activity: FragmentActivity, type: AuthTypes): Maybe<String> {
        if (activity !is IRxActivity) {
            return Maybe.empty()

        }
        return getVkResult(activity)
    }

    private fun getVkResult(activity: IRxActivity): Maybe<String> {
        return activity
                .rxActivityResult
                .filter { it.resultCode == Activity.RESULT_OK }
                .flatMap { result ->
                    Flowable.create<String>({ emitter ->
                        val intentData = result.intent ?: Intent()
                        VK.onActivityResult(result.requestCode, result.resultCode, intentData, object : VKAuthCallback {
                            override fun onLogin(token: VKAccessToken) {
                                Log.e("DD", "onLogin token ${token.isValid} ${token.accessToken}")
                                if (token.isValid) {
                                    emitter.onNext(token.accessToken)
                                    emitter.onComplete()
                                }
                            }

                            override fun onLoginFailed(errorCode: Int) {
                                Log.e("DD", "onLogin failed")
                                //do nothing, let user choose another option
                            }
                        })
                    }, BackpressureStrategy.LATEST)
                }
                .firstElement()
    }
}