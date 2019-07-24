package com.simapp.testapp.auth.domain

import io.reactivex.Flowable
import io.reactivex.Maybe

interface IAuthUseCases {
    fun getAvailableAuthNetworks(): List<AuthTypes>
    fun saveAuthData(type: AuthTypes, token: String)
    fun onLoginFlow(): Flowable<Boolean>
    fun removeAuthData()
    fun getUser(): Maybe<LoadUserResult>
    fun hasAuthData(): Boolean
}