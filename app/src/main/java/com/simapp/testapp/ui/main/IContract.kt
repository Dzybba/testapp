package com.simapp.testapp.ui.main

import com.simapp.base.presentation.CleanView

interface IContract {
    interface IMainView: CleanView {
        fun showAuthView()
        fun showGitHubView()
    }
    interface IMainViewPresenter {
        fun onViewCreated()
    }
}