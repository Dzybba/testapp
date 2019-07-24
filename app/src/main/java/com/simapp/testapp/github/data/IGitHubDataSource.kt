package com.simapp.testapp.github.data

import com.simapp.testapp.github.domain.GithubSearchResult
import io.reactivex.Maybe

interface IGitHubDataSource {
    fun loadUsers(query: String, page: Int, perPage: Int): Maybe<GithubSearchResult>
}