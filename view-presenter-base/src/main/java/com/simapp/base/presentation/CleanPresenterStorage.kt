package com.simapp.base.presentation

import android.arch.lifecycle.ViewModel
import javax.inject.Inject

open class CleanPresenterStorage<V : CleanView, P : BaseCleanPresenter<V>?> @Inject constructor(): ViewModel() {
    var presenter: P? = null
        @Inject
        set(value) {
            field = value
        }
    override fun onCleared() {
        presenter?.onPresenterDestroy()
        presenter = null
    }
}