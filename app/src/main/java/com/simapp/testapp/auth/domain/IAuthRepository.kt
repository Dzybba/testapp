package com.simapp.testapp.auth.domain

import io.reactivex.Maybe

interface IAuthRepository {
    fun saveAuthData(type: AuthTypes, token: String)
    fun removeAuthData()
    fun loadUser(): Maybe<User>
    fun hasAuthData(): Boolean
}