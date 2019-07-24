package com.simapp.testapp.github.domain

import io.reactivex.Maybe

interface IGitHubUseCases {
    fun searchUsers(query: String, start: Int, len: Int): Maybe<List<GitHubUser>>
}