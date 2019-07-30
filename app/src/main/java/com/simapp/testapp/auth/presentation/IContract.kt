package com.simapp.testapp.auth.presentation

import com.simapp.base.presentation.CleanPresenter
import com.simapp.base.presentation.CleanView
import com.simapp.testapp.auth.domain.AuthTypes

interface IContract {
    interface IAuthView: CleanView {
        fun submitAuthList(list: List<AuthListItem>)
    }

    interface IPresenter: CleanPresenter<IAuthView> {
        fun onItemClick(type: AuthTypes)
    }
}