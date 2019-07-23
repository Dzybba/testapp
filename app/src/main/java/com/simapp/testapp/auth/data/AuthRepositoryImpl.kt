package com.simapp.testapp.auth.data

import android.content.Context
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


@Singleton
class AuthRepositoryImpl @Inject constructor(
        context: Context
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
        when (type) {
            AuthTypes.VK -> requestVkUser()
            AuthTypes.FB -> {}
            AuthTypes.GOOGLE -> TODO()
        }
    }

    private fun requestVkUser() {
        val request = VKRequest<JSONObject>(VK_REQUEST_METHOD)
        request.addParam("fields", VK_FIELDS)
        VK.execute(request, object : VKApiCallback<JSONObject> {
            override fun fail(error: VKApiExecutionException) {
                //ошибочка
                Log.e("DD", "error load user")
            }

            override fun success(result: JSONObject) {
                try {
                    val jsonObject = result.getJSONArray("response").getJSONObject(0)
                    val user = Gson().fromJson(jsonObject.toString(), VkUser::class.java)
                    userProcessor.onNext(User("${user.first_name} ${user.last_name} ${user.screen_name}", user.photo_100))
                } catch (th: Throwable) {
                    //ошибочка
                }
            }
        })

    }

    override fun hasAuthData(): Boolean {
        return prefs.contains(AUTH_TYPE_KEY) && prefs.contains(AUTH_TOKEN_KEY)
    }

    companion object {
        private const val AUTH_TYPE_KEY = "AUTH_TYPE_KEY"
        private const val AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY"
        private const val VK_FIELDS = "first_name,last_name,photo_100,screen_name"
        private const val VK_REQUEST_METHOD = "users.get"
    }

    data class VkUser(
            val first_name: String = "",
            val last_name: String = "",
            val screen_name: String = "",
            val photo_100: String = ""
    )
}