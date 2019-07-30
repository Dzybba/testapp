package com.simapp.testapp.auth.navigator

import android.support.v4.app.FragmentActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.simapp.testapp.R
import com.simapp.testapp.main.IRxActivity
import io.reactivex.Maybe
import javax.inject.Inject

class FbAuthNavigatorImpl  @Inject constructor(): IAuthNetworkNavigator {
    private var callbackManager: CallbackManager? = null

    override fun runAuth(activity: FragmentActivity): Maybe<String> {
        FacebookSdk.setApplicationId(activity.resources.getString(R.string.fb_id))
        FacebookSdk.sdkInitialize(activity.applicationContext)
        val loginManager = LoginManager.getInstance()
        callbackManager = CallbackManager.Factory.create()
        val currentAccessToken = AccessToken.getCurrentAccessToken()
        return if (currentAccessToken != null) {
            return if (currentAccessToken.isExpired) {
                Maybe.create<String> { emitter ->
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
        } else {
            loginInFb(activity, loginManager)
        }
    }

    private fun loginInFb(activity: FragmentActivity, loginManager: LoginManager): Maybe<String> {
        loginManager.logInWithReadPermissions(activity, listOf("public_profile, user_photos"))
        return getResult(activity)
    }

    override fun getResult(activity: FragmentActivity): Maybe<String> {
        if (activity !is IRxActivity) return Maybe.empty()
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