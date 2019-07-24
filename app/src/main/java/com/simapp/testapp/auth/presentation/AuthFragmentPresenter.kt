package com.simapp.testapp.auth.presentation

import android.util.Log
import com.simapp.clean.base.presentation.BaseCleanPresenter
import com.simapp.testapp.auth.domain.AuthTypes
import com.simapp.testapp.auth.domain.IAuthUseCases
import com.simapp.testapp.auth.navigator.IAuthNavigator
import com.simapp.testapp.github.domain.IGitHubUseCases
import io.reactivex.Maybe
import javax.inject.Inject

class AuthFragmentPresenter @Inject constructor(
        private val authUseCases: IAuthUseCases,
        private val navigator: IAuthNavigator
) : BaseCleanPresenter<IContract.IAuthView>() {

    private var requestingAuthType: AuthTypes? = null

    override fun onCreate() {
        super.onCreate()
        authUseCases
                .getAvailableAuthNetworks()
                .map { type ->
                    AuthListItem(type,
                            when (type) {
                                AuthTypes.VK -> "Вконтакте"
                                AuthTypes.FB -> "FaceBook"
                                AuthTypes.GOOGLE -> "Google"
                            })
                }
                .also { list ->
                    view?.submitAuthList(list)
                }
        //подписываемся на результат при повороте экрана
        requestingAuthType?.also { type ->
            withActivity {
                navigator
                        .getResult(it, type)
                        .handleNavigatorResult(type)
            }
        }
    }

    fun onItemClick(type: AuthTypes) {
        withActivity {
            requestingAuthType = type
            navigator
                    .runAuth(it, type)
                    .handleNavigatorResult(type)
        }
    }

    private fun Maybe<String>.handleNavigatorResult(type: AuthTypes) {
        this
                .subscribe(
                        { token ->
                            requestingAuthType = null
                            authUseCases.saveAuthData(type, token)
                            authUseCases
                                    .getUser()
                                    .subscribe(
                                            {
                                                Log.e("DD", "load user result $it")
                                            },
                                            {
                                                //исключение
                                            }
                                    )
                        },
                        {
                            //log error
                            requestingAuthType = null
                        }
                )
                .also { onCreateViewSubscription.add(it) }
    }

}