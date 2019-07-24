package com.simapp.testapp.auth.domain

import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCasesImpl @Inject constructor(
        private val repository: IAuthRepository
): IAuthUseCases {

    private val loginProcessor = PublishProcessor.create<Boolean>()

    override fun getAvailableAuthNetworks(): List<AuthTypes> {
        return AuthTypes.values().toList()
    }

    override fun saveAuthData(type: AuthTypes, token: String) {
        repository.saveAuthData(type, token)
        loginProcessor.onNext(true)
    }

    override fun removeAuthData() {
        repository.removeAuthData()
        loginProcessor.onNext(false)
    }

    override fun getUser(): Maybe<LoadUserResult> {
        return repository.loadUser()
    }

    override fun onLoginFlow(): Flowable<Boolean> {
        return loginProcessor
    }

    override fun hasAuthData(): Boolean {
        return repository.hasAuthData()
    }
}