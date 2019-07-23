package com.simapp.testapp.auth.presentation

import com.simapp.testapp.auth.domain.AuthTypes

data class AuthListItem(
        val type: AuthTypes,
        val name: String
)