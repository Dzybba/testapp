package com.simapp.testapp.github.domain

import io.reactivex.Maybe

interface IGitHubRepository {
    fun searchUsers(query: String): Maybe<List<GitHubUser>>
}