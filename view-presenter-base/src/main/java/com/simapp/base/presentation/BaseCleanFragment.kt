package com.simapp.base.presentation


import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.simapp.base.dagger.DaggerViewModelFactory
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


open class DaggerBaseCleanFragment<V : CleanView, P : BaseCleanPresenter<V>> : Fragment(), CleanView {

    var daggerViewModelFactory: DaggerViewModelFactory<CleanPresenterStorage<V, P>>? = null
        @Inject
        set(value) {
            field = value
        }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

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
        privateImpl.onCreate(this, daggerViewModelFactory)
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
}

abstract class DaggerBaseCleanDialogFragment<V : CleanView, P : BaseCleanPresenter<V>> : DialogFragment(), CleanView {

    var daggerViewModelFactory: DaggerViewModelFactory<CleanPresenterStorage<V, P>>? = null
        @Inject
        set(value) {
            field = value
        }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

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
        privateImpl.onCreate(this, daggerViewModelFactory)
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
}

open class DaggerBaseCleanBottomSheetFragment<V : CleanView, P : BaseCleanPresenter<V>> : BottomSheetDialogFragment(), CleanView {

    var daggerViewModelFactory: DaggerViewModelFactory<CleanPresenterStorage<V, P>>? = null
        @Inject
        set(value) {
            field = value
        }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

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
        privateImpl.onCreate(this, daggerViewModelFactory)
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
}