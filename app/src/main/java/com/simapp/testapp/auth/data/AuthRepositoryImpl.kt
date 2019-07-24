package com.simapp.testapp.auth.data

import android.content.Context
import android.preference.PreferenceManager
import com.simapp.testapp.auth.domain.*
import io.reactivex.Maybe
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Singleton
class AuthRepositoryImpl @Inject constructor(
        context: Context,
        @Named("VK")
        private val vkServerDataSource: IServerDataSource,
        @Named("FB")
        private val fbServerDataSource: IServerDataSource,
        @Named("GOOGLE")
        private val googleDataSource: IServerDataSource
) : IAuthRepository {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    private val userProcessor = BehaviorProcessor.create<User>()

    override fun saveAuthData(type: AuthTypes, token: String) {
        prefs
                .edit()
                .putString(AUTH_TYPE_KEY, type.name)
                .putString(AUTH_TOKEN_KEY, token)
                .apply()
    }

    override fun removeAuthData() {
        prefs
                .edit()
                .remove(AUTH_TOKEN_KEY)
                .remove(AUTH_TYPE_KEY)
                .apply()
    }

    override fun loadUser(): Maybe<LoadUserResult> {
        if (!userProcessor.hasValue()) {
            //load
            val stringType = prefs.getString(AUTH_TYPE_KEY, "")
            val type = AuthTypes.values().find { it.name == stringType }
            return if (type == null) {
                Maybe.just(LoadUserResult(null, AuthErrors.ERROR_TOKEN_EMPTY))
            } else {
                requestUser(type)
            }
        }
        return userProcessor.firstElement().map { LoadUserResult(it) }
    }

    private fun requestUser(type: AuthTypes): Maybe<LoadUserResult> {
        return getDataSource(type)
                .requestUser()
                .doOnSuccess {
                    userProcessor.onNext(it)
                }
                .map { LoadUserResult(it) }
                .switchIfEmpty(Maybe.just(LoadUserResult(null, AuthErrors.ERROR_LOAD_USER)))
    }

    private fun getDataSource(type: AuthTypes): IServerDataSource {
        return when (type) {
            AuthTypes.VK -> vkServerDataSource
            AuthTypes.FB -> fbServerDataSource
            AuthTypes.GOOGLE -> googleDataSource
        }
    }

    override fun hasAuthData(): Boolean {
        return prefs.contains(AUTH_TYPE_KEY) && prefs.contains(AUTH_TOKEN_KEY)
    }

    companion object {
        private const val AUTH_TYPE_KEY = "AUTH_TYPE_KEY"
        private const val AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY"
    }

}