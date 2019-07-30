package com.simapp.testapp.main

import com.simapp.base.presentation.BaseCleanPresenter
import com.simapp.testapp.auth.domain.AuthErrors
import com.simapp.testapp.auth.domain.IAuthUseCases
import dagger.Binds
import dagger.Module
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@Module
abstract class MainFragmentPresenterModule {
    @Binds
    abstract fun getPresenter(presenter: MainFragmentPresenter): IContract.IMainViewPresenter
}

class MainFragmentPresenter @Inject constructor(
        private val authUseCases: IAuthUseCases
) : BaseCleanPresenter<IContract.IMainView>(), IContract.IMainViewPresenter {

    override fun onViewCreated() {
        if (!authUseCases.hasAuthData()) {
            view?.showAuthView()
        } else {
            tryGetUser()
        }
        authUseCases
                .onLoginFlow()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { login ->
                            if (login) {
                                tryGetUser()
                            } else {
                                view?.showAuthView()
                            }
                        },
                        {}
                ).also {
                    onCreateViewSubscription.add(it)
                }
    }

    private fun tryGetUser() {
        authUseCases
                .getUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if (it.user != null) {
                                view?.showGitHubView()
                            } else {
                                view?.showAuthView()
                                when (it.error) {
                                    AuthErrors.NO_ERROR -> {
                                    }
                                    AuthErrors.ERROR_LOAD_USER -> {
                                        //show error
                                    }
                                    AuthErrors.ERROR_TOKEN_EMPTY -> {
                                        //show error
                                    }
                                }
                            }
                        },
                        {}
                ).also {
                    onCreateViewSubscription.add(it)
                }
    }

}