package com.simapp.clean.base.presentation


import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.simapp.clean.base.dagger.DaggerViewModelFactory
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class DaggerBaseCleanFragment<V : CleanView, P : BaseCleanPresenter<V>> : BaseCleanFragment<V, P>() {
    var daggerViewModelFactory: DaggerViewModelFactory<CleanPresenterStorage<V, P>>? = null
        @Inject
        set(value) {
            field = value
            viewModelFactory = field
        }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}

abstract class BaseCleanFragment<V : CleanView, P : BaseCleanPresenter<V>>(optionalGetter: (() -> P?)? = null) : Fragment(), CleanView {

    var viewModelFactory: ViewModelProvider.Factory? = null

    private val privateImpl = BaseCleanViewPrivateImpl(optionalGetter)

    val presenter: P?
        get() = privateImpl.presenter

    val onCreateSubscription: CompositeDisposable
        get() = privateImpl.onCreateSubscription

    val onStartSubscription: CompositeDisposable
        get() = privateImpl.onStartSubscription

    override fun getSupportFragmentManager(): FragmentManager? = fragmentManager

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        privateImpl.onCreate(this, viewModelFactory) { initPresenter() }
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        privateImpl.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        privateImpl.onStop()
    }

    open fun initPresenter(): P? {
        return null
    }
}

abstract class DaggerBaseCleanDialogFragment<V : CleanView, P : BaseCleanPresenter<V>> : BaseCleanDialogFragment<V, P>() {
    var daggerViewModelFactory: DaggerViewModelFactory<CleanPresenterStorage<V, P>>? = null
        @Inject
        set(value) {
            field = value
            viewModelFactory = field
        }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}

abstract class BaseCleanDialogFragment<V : CleanView, P : BaseCleanPresenter<V>> : DialogFragment(), CleanView {

    var viewModelFactory: ViewModelProvider.Factory? = null

    private val privateImpl = BaseCleanViewPrivateImpl<V, P>()

    val presenter: P?
        get() = privateImpl.presenter

    val onCreateSubscription: CompositeDisposable
        get() = privateImpl.onCreateSubscription

    val onStartSubscription: CompositeDisposable
        get() = privateImpl.onStartSubscription

    override fun getSupportFragmentManager(): FragmentManager? = fragmentManager

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        privateImpl.onCreate(this, viewModelFactory) { initPresenter() }
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        privateImpl.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        privateImpl.onStop()
    }

    open fun initPresenter(): P? {
        return null
    }
}

abstract class DaggerBaseCleanBottomSheetFragment<V : CleanView, P : BaseCleanPresenter<V>> : BaseCleanBottomSheetFragment<V, P>() {
    var daggerViewModelFactory: DaggerViewModelFactory<CleanPresenterStorage<V, P>>? = null
        @Inject
        set(value) {
            field = value
            viewModelFactory = field
        }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}

abstract class BaseCleanBottomSheetFragment<V : CleanView, P : BaseCleanPresenter<V>> : BottomSheetDialogFragment(), CleanView {

    var viewModelFactory: ViewModelProvider.Factory? = null

    private val privateImpl = BaseCleanViewPrivateImpl<V, P>()

    override fun getSupportFragmentManager(): FragmentManager? = fragmentManager

    val presenter: P?
        get() = privateImpl.presenter

    val onCreateSubscription: CompositeDisposable
        get() = privateImpl.onCreateSubscription

    val onStartSubscription: CompositeDisposable
        get() = privateImpl.onStartSubscription

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        privateImpl.onCreate(this, viewModelFactory) { initPresenter() }
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        privateImpl.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        privateImpl.onStop()
    }

    open fun initPresenter(): P? {
        return null
    }
}