package com.simapp.base.presentation

import android.arch.lifecycle.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import io.reactivex.disposables.CompositeDisposable

class BaseCleanViewPrivateImpl<V : CleanView, P : BaseCleanPresenter<V>> {

    private lateinit var publicImpl: CleanView

    var presenter: P? = null
    val onCreateSubscription = CompositeDisposable()
    val onStartSubscription = CompositeDisposable()

    fun onCreate(publicImpl: CleanView, viewModelFactory: ViewModelProvider.Factory? = null) {
        this.publicImpl = publicImpl
        val viewModel = getViewModelProvider(viewModelFactory).get(getBaseViewModelClass<CleanPresenterStorage<V, P>>())
        presenter = viewModel.presenter
        presenter?.attachPresentedView(publicImpl as V)
        presenter?.attachLifecycle(getLifecycle())
    }

    fun onDestroy() {
        onCreateSubscription.dispose()
        presenter?.detachLifecycle(getLifecycle())
        presenter?.detachView()
    }

    fun onStart() {
    }

    fun onStop() {
        onStartSubscription.clear()
    }

    private fun getViewModelProvider(viewModelFactory: ViewModelProvider.Factory?): ViewModelProvider {
        return when (publicImpl) {
            is FragmentActivity -> ViewModelProviders.of(publicImpl as FragmentActivity, viewModelFactory)
            is Fragment -> ViewModelProviders.of(publicImpl as Fragment, viewModelFactory)
            else -> throw IllegalStateException("Wrong public implementation class")
        }
    }

    private fun getLifecycle(): Lifecycle {
        if (publicImpl is LifecycleOwner) {
            return (publicImpl as LifecycleOwner).lifecycle
        } else {
            throw IllegalStateException("Wrong public implementation class")
        }
    }

    private inline fun <reified T> getBaseViewModelClass() = T::class.java
}