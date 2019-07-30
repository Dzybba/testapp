package com.simapp.testapp.auth.presentation

import com.simapp.base.presentation.CleanView

interface IContract {
    interface IAuthView: CleanView {
        fun submitAuthList(list: List<AuthListItem>)
    }
}