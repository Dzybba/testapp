package com.simapp.testapp.auth.domain

enum class AuthTypes {
    VK,
    FB,
    GOOGLE
}

data class User(
        val name: String,
        val photoId: Long
)