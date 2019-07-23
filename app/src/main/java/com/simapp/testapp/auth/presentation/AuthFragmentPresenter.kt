package com.simapp.testapp.auth.presentation

import com.simapp.clean.base.presentation.BaseCleanPresenter
import com.simapp.testapp.auth.domain.AuthTypes
import com.simapp.testapp.auth.domain.IAuthUseCases
import com.simapp.testapp.auth.navigator.IAuthNavigator
import javax.inject.Inject

class AuthFragmentPresenter @Inject constructor(
        private val authUseCases: IAuthUseCases,
        private val navigator: IAuthNavigator
) : BaseCleanPresenter<IContract.IAuthView>() {

    override fun onCreate() {
        super.onCreate()
        authUseCases
                .getAvailableAuthNetworks()
                .map { type ->
                    AuthListItem(type,
                            when(type) {
                                AuthTypes.VK -> "Вконтакте"
                                AuthTypes.FB -> "FaceBook"
                                AuthTypes.GOOGLE -> "Google"
                            })
                }
                .also { list ->
                    view?.submintAuthList(list)
                }
    }

    fun onItemClick(type: AuthTypes) {
        withActivity {
            onCreateViewSubscription.add(
                    navigator
                            .runAuth(it, type)
                            .subscribe(
                                    { token ->
                                        authUseCases.saveAuthData(type, token)
                                    },
                                    {
                                        //log error
                                    }
                            )
            )
        }
    }

}