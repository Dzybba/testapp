package com.simapp.testapp.auth.presentation

import com.simapp.base.presentation.BaseCleanPresenter
import com.simapp.testapp.auth.domain.AuthTypes
import com.simapp.testapp.auth.domain.IAuthUseCases
import com.simapp.testapp.auth.navigator.IAuthNavigator
import dagger.Binds
import dagger.Module
import io.reactivex.Maybe
import javax.inject.Inject

@Module
abstract class AuthFragmentPresenterModule {
    @Binds
    abstract fun getPresenter(preseneter: AuthFragmentPresenter): IContract.IPresenter
}

class AuthFragmentPresenter @Inject constructor(
        private val authUseCases: IAuthUseCases,
        private val navigator: IAuthNavigator
) : BaseCleanPresenter<IContract.IAuthView>(), IContract.IPresenter {

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


    override fun onItemClick(type: AuthTypes) {
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
                        },
                        {
                            //log error
                            requestingAuthType = null
                        }
                )
                .also { onCreateViewSubscription.add(it) }
    }

}