package com.simapp.testapp.auth.navigator

import android.app.Activity
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.simapp.testapp.R
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

    private var callbackManager: CallbackManager? = null

    override fun runAuth(activity: FragmentActivity, type: AuthTypes): Maybe<String> {
        return when (type) {
            AuthTypes.VK -> {
                vkAuth(activity)
            }
            AuthTypes.FB -> {
                fbAuth(activity)
            }
            AuthTypes.GOOGLE -> {
                Maybe.empty()
            }
        }
    }

    private fun vkAuth(activity: FragmentActivity): Maybe<String> {
        val splitPermissions = ""
        val vkScopeList = VKScope.values()
                .filter { splitPermissions.contains(it.name.toLowerCase()) }

        VK.login(activity, vkScopeList)
        if (activity !is IRxActivity) {
            return Maybe.empty()

        }
        return getVkResult(activity)
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

    private fun fbAuth(activity: FragmentActivity): Maybe<String> {
        FacebookSdk.setApplicationId(activity.resources.getString(R.string.fb_id))
        FacebookSdk.sdkInitialize(activity.applicationContext)
        val loginManager = LoginManager.getInstance()
        callbackManager = CallbackManager.Factory.create()
        val currentAccessToken = AccessToken.getCurrentAccessToken()
        if (currentAccessToken != null) {
            if (currentAccessToken.isExpired) {
                return Maybe.create<String> { emitter ->
                    AccessToken.refreshCurrentAccessTokenAsync(object : AccessToken.AccessTokenRefreshCallback {
                        override fun OnTokenRefreshed(accessToken: AccessToken) {
                            emitter.onSuccess(accessToken.token)
                        }

                        override fun OnTokenRefreshFailed(exception: FacebookException) {
                            emitter.onComplete()
                        }
                    })
                }
                        .switchIfEmpty(loginInFb(activity, loginManager))
            } else {
                Maybe.just(currentAccessToken.token)
            }
        }
        return loginInFb(activity, loginManager)
    }

    private fun loginInFb(activity: FragmentActivity, loginManager: LoginManager): Maybe<String> {
        activity as IRxActivity
        loginManager.logInWithReadPermissions(activity, listOf("public_profile"))
        return activity
                .rxActivityResult
                .doOnNext {
                    callbackManager?.onActivityResult(it.requestCode, it.resultCode, it.intent)
                }
                .firstElement()
                .map {
                    val currentAccessToken = AccessToken.getCurrentAccessToken()
                    currentAccessToken.token
                }
    }

}