package com.simapp.testapp.auth.data

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson
import com.simapp.testapp.auth.domain.AuthTypes
import com.simapp.testapp.auth.domain.IAuthRepository
import com.simapp.testapp.auth.domain.User
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import com.vk.api.sdk.requests.VKRequest
import io.reactivex.Maybe
import io.reactivex.processors.BehaviorProcessor
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton
import com.facebook.GraphRequest
import com.facebook.AccessToken
import com.facebook.HttpMethod
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Named


@Singleton
class AuthRepositoryImpl @Inject constructor(
        context: Context,
        @Named("VK")
        private val vkServerDataSource: IServerDataSource,
        @Named("FB")
        private val fbServerDataSource: IServerDataSource
) : IAuthRepository {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    private val userProcessor = BehaviorProcessor.create<User>()

    private val compositeDisposable = CompositeDisposable()

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

    override fun loadUser(): Maybe<User> {
        if (!userProcessor.hasValue()) {
            //load
            val stringType = prefs.getString(AUTH_TYPE_KEY, "")
            val type = AuthTypes.values().find { it.name == stringType }
            if (type == null) {
                //ошибочка
            } else {
                requestUser(type)
            }
        }
        return userProcessor.firstElement()
    }

    private fun requestUser(type: AuthTypes) {
        getDataSource(type)
                ?.requestUser()
                ?.subscribe(
                        {
                            userProcessor.onNext(it)
                        },
                        {

                        })
                ?.also {
                    compositeDisposable.add(it)
                }

    }

    private fun getDataSource(type: AuthTypes): IServerDataSource? {
        return when (type) {
            AuthTypes.VK -> vkServerDataSource
            AuthTypes.FB -> fbServerDataSource
            AuthTypes.GOOGLE -> null
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