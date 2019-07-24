package com.simapp.testapp.github.domain

import io.reactivex.Flowable

interface IGitHubUseCases {
    fun searchUsers(query: String)
    fun getSearchUsersFlow(query: String): Flowable<List<GitHubUser>>
    fun hasMore(query: String): Boolean
    fun hasResults(query: String): Boolean
}