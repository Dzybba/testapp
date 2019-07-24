package com.simapp.testapp.auth.domain

import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCasesImpl @Inject constructor(
        private val repository: IAuthRepository
): IAuthUseCases {

    override fun getAvailableAuthNetworks(): List<AuthTypes> {
        return AuthTypes.values().toList()
    }

    override fun saveAuthData(type: AuthTypes, token: String) {
        repository.saveAuthData(type, token)
    }

    override fun removeAuthData() {
        repository.removeAuthData()
    }

    override fun getUser(): Maybe<LoadUserResult> {
        return repository.loadUser()
    }

    override fun hasAuthData(): Boolean {
        return repository.hasAuthData()
    }
}