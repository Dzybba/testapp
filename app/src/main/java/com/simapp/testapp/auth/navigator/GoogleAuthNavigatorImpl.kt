package com.simapp.testapp.auth.navigator

import android.support.v4.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.simapp.testapp.main.IRxActivity
import io.reactivex.Maybe
import javax.inject.Inject

class GoogleAuthNavigatorImpl @Inject constructor(): IAuthNetworkNavigator {

    override fun runAuth(activity: FragmentActivity): Maybe<String> {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build()
        val client = GoogleSignIn.getClient(activity, gso)

        val signInIntent = client.signInIntent
        activity.startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
        return getResult(activity)
    }

    override fun getResult(activity: FragmentActivity): Maybe<String> {
        if (activity !is IRxActivity) return Maybe.empty()
        return activity
                .rxActivityResult
                .filter { it.requestCode == GOOGLE_SIGN_IN_REQUEST_CODE }
                .firstElement()
                .flatMap { result ->
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.intent)
                    return@flatMap try {
                        val account = task.getResult(ApiException::class.java)
                        account?.id?.let {
                            Maybe.just(it)
                        } ?: Maybe.empty()
                    } catch (e: ApiException) {
                        // The ApiException status code indicates the detailed failure reason.
                        // Please refer to the GoogleSignInStatusCodes class reference for more information.
                        Maybe.empty<String>()
                    }
                }
    }

    companion object {
        private const val GOOGLE_SIGN_IN_REQUEST_CODE = 400
    }
}