package com.simapp.testapp.auth.domain

enum class AuthTypes {
    VK,
    FB,
    GOOGLE
}

data class User(
        val name: String,
        val photoUrl: String
)

enum class AuthErrors {
    NO_ERROR,
    ERROR_LOAD_USER,
    ERROR_TOKEN_EMPTY
}

data class LoadUserResult(
        val user: User?,
        val error: AuthErrors = AuthErrors.NO_ERROR
)
