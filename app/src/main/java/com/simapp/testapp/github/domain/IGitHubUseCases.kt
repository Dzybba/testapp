package com.simapp.testapp.github.domain

import io.reactivex.Maybe

interface IGitHubUseCases {
    fun searchUsers(query: String, offset: Int, len: Int): Maybe<List<GitHubUser>>
    fun hasMore(query: String, offset: Int): Boolean
}