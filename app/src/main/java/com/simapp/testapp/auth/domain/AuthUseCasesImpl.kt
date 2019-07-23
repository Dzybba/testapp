package com.simapp.testapp.auth.domain

import android.util.Log
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCasesImpl @Inject constructor(): IAuthUseCases {
    override fun getAvailableAuthNetworks(): List<AuthTypes> {
        Log.e("DD", "get networks $this")
        return AuthTypes.values().toList()
    }

    override fun saveAuthData(type: AuthTypes, token: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeAuthData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadUser(): Maybe<User> {
        return Maybe.empty()
    }
}