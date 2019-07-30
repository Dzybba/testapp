package com.simapp.testapp.main

import com.simapp.base.presentation.CleanPresenter
import com.simapp.base.presentation.CleanView

interface IContract {
    interface IMainView: CleanView {
        fun showAuthView()
        fun showGitHubView()
    }
    interface IMainViewPresenter: CleanPresenter<IMainView>{
        fun onViewCreated()
    }
}