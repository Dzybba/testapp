package com.simapp.base.presentation

import android.arch.lifecycle.Lifecycle
import android.os.Bundle


interface CleanPresenter<V : CleanView> {

    fun getStateBundle(): Bundle

    fun attachLifecycle(lifecycle: Lifecycle)

    fun detachLifecycle(lifecycle: Lifecycle)

    fun attachPresentedView(view: V)

    fun getPresentedView(): V?

    fun detachView()

    fun isViewAttached(): Boolean

    fun onPresenterCreated()

    fun onPresenterDestroy()
}