package com.simapp.testapp.github.domain

import io.reactivex.Flowable

interface IGitHubRepository {
    fun searchUsers(query: String)
    fun getSearchUsersFlow(query: String): Flowable<List<GitHubUser>>
    fun getHasMore(query: String): Boolean
    fun getQueryExist(query: String): Boolean
}