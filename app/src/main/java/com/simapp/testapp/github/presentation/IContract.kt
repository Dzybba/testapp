package com.simapp.testapp.github.presentation

import com.simapp.base.presentation.CleanPresenter
import com.simapp.base.presentation.CleanView
import com.simapp.testapp.auth.domain.User
import com.simapp.testapp.github.domain.GitHubUser

interface IContract {
    interface IGitHubSearchView: CleanView {
        fun setVisibleNextButton(visible: Boolean)
        fun submitList(list: List<GitHubUser>)
        fun setCurrentUser(currentUser: User)
    }
    interface IGitHubSearchPresenter: CleanPresenter<IGitHubSearchView> {
        fun onSearchQuery(query: String)
        fun getCurrentQuery(): String
        fun nextPage()
        fun onExitClicked()
    }
}