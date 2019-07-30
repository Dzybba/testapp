package com.simapp.base.presentation

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.os.Bundle


interface CleanPresenter<V : CleanView>: LifecycleObserver {

    fun attachLifecycle(lifecycle: Lifecycle)

    fun detachLifecycle(lifecycle: Lifecycle)

    fun attachPresentedView(view: V)

    fun getPresentedView(): V?

    fun detachView()

    fun isViewAttached(): Boolean

    fun onPresenterDestroy()
}