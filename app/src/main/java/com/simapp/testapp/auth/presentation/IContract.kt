package com.simapp.testapp.auth.presentation

import com.simapp.clean.base.presentation.CleanView

interface IContract {
    interface IAuthView: CleanView {
        fun submintAuthList(list: List<AuthListItem>)
    }
}