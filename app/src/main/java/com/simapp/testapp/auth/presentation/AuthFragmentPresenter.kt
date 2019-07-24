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
        private val navigator: IAuthNavigator,
        private val gitUseCases: IGitHubUseCases
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
                    view?.submintAuthList(list)
                }
        //подписываемся на результат при повороте экрана
        requestingAuthType?.also { type ->
            withActivity {
                navigator
                        .getResult(it, type)
                        .handleNavigatorResult(type)
            }
        }
        gitUseCases
                .searchUsers("Den", 0, 20)
                .subscribe(
                        {
                            Log.e("DD", "result $it")
                        },
                        {
                            Log.e("DD", "error $it")
                        }
                )
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
                                    .loadUser()
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