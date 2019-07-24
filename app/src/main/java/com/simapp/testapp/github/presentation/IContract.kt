package com.simapp.testapp.github.presentation

import com.simapp.clean.base.presentation.CleanView
import com.simapp.testapp.github.domain.GitHubUser

interface IContract {
    interface IGitHubSearchView: CleanView {
        fun setVisibleNextButton(visible: Boolean)
        fun submitList(list: List<GitHubUser>)
    }
    interface IGitHubSearchPresenter {
        fun onSearchQuery(query: String)
        fun getCurrentQuery(): String
        fun nextPage()
    }
}