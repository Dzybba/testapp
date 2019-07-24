package com.simapp.testapp.auth.data

import android.os.Bundle
import android.util.Log
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.google.gson.Gson
import com.simapp.testapp.auth.domain.User
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FbServerDataSource @Inject constructor() : IServerDataSource {
    override fun requestUser(): Maybe<User> {
        val currentAccessToken = AccessToken.getCurrentAccessToken()
        return requestProfile(currentAccessToken)
    }

    private fun requestProfile(accessToken: AccessToken): Maybe<User> {
        val parameters = Bundle()
        parameters.putString("fields", "name, picture{url}")
        return Maybe.create { emitter ->
            GraphRequest(
                    accessToken,
                    "me",
                    parameters,
                    HttpMethod.GET,
                    GraphRequest.Callback { response ->
                        if (response != null) {
                            val jsonObject = response.jsonObject
                            try {
                                val user = Gson().fromJson(jsonObject.toString(), FbUser::class.java)
                                val url = user.picture["data"]?.get("url") ?: ""
                                emitter.onSuccess(User(user.name, url))
                            } catch (th: Throwable) {
                                emitter.onComplete()
                            }
                        }
                    }
            ).executeAsync()
        }
    }


    private data class FbUser(
            val name: String = "",
            val picture: Map<String, Map<String, String>>
    )

}