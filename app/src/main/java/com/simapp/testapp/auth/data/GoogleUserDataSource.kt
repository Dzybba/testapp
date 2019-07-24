package com.simapp.testapp.auth.data

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.simapp.testapp.auth.domain.User
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GoogleUserDataSource @Inject constructor(private val context: Context) : IServerDataSource {
    override fun requestUser(): Maybe<User> {
        return GoogleSignIn.getLastSignedInAccount(context)?.let {
            Maybe.just(User(it.displayName ?: "", it.photoUrl?.toString() ?: ""))
        } ?: Maybe.empty()
    }
}