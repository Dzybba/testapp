package com.simapp.base.presentation

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import io.reactivex.disposables.CompositeDisposable


open class BaseCleanPresenter<V : CleanView> : LifecycleObserver, CleanPresenter<V> {

    private var stateBundle: Bundle = Bundle()
    var view: V? = null

    protected val onCreatePresenterSubscription = CompositeDisposable()
    protected val onCreateViewSubscription = CompositeDisposable()
    protected val onStartViewSubscription = CompositeDisposable()

    final override fun attachLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    final override fun detachLifecycle(lifecycle: Lifecycle) {
        lifecycle.removeObserver(this)
    }

    final override fun getPresentedView(): V? {
        return view
    }

    final override fun attachPresentedView(view: V) {
        this.view = view
    }

    final override fun detachView() {
        view = null
    }

    final override fun isViewAttached(): Boolean {
        return view != null
    }

    final override fun getStateBundle(): Bundle {
        return stateBundle
    }

    override fun onPresenterDestroy() {
        stateBundle.clear()
        onCreatePresenterSubscription.dispose()
    }

    override fun onPresenterCreated() {
        // do nothing
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected open fun onStart() {
        //to override
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected open fun onStop() {
        //to override
        onStartViewSubscription.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected open fun onCreate() {
        //to override
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected open fun onDestroy() {
        onCreateViewSubscription.clear()
    }

    fun withActivity(block: (activity: FragmentActivity) -> Unit) {
        view?.getActivity()?.let(block)
    }

    fun withFragmentManager(block: (fm: FragmentManager) -> Unit) {
        withActivity {
            block(it.supportFragmentManager)
        }
    }
}