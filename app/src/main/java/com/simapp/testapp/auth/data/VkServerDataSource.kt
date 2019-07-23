package com.simapp.testapp.auth.data

import android.util.Log
import com.google.gson.Gson
import com.simapp.testapp.auth.domain.User
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import com.vk.api.sdk.requests.VKRequest
import io.reactivex.Maybe
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VkServerDataSource @Inject constructor() : IServerDataSource {

    override fun requestUser(): Maybe<User> {
        return Maybe.create { emitter ->
            val request = VKRequest<JSONObject>(VK_REQUEST_METHOD)
            request.addParam("fields", VK_FIELDS)
            VK.execute(request, object : VKApiCallback<JSONObject> {
                override fun fail(error: VKApiExecutionException) {
                    //ошибочка
                    Log.e("DD", "error load user")
                    emitter.onComplete()
                }

                override fun success(result: JSONObject) {
                    try {
                        val jsonObject = result.getJSONArray("response").getJSONObject(0)
                        val user = Gson().fromJson(jsonObject.toString(), VkUser::class.java)
                        emitter.onSuccess(User("${user.first_name} ${user.last_name} ${user.screen_name}", user.photo_100))
                    } catch (th: Throwable) {
                        //ошибочка
                        emitter.onComplete()
                    }
                }
            })
        }
    }

    private data class VkUser(
            val first_name: String = "",
            val last_name: String = "",
            val screen_name: String = "",
            val photo_100: String = ""
    )

    companion object {
        private const val VK_FIELDS = "first_name,last_name,photo_100,screen_name"
        private const val VK_REQUEST_METHOD = "users.get"
    }
}