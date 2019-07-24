package com.simapp.testapp.github.domain

import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubUseCasesImpl @Inject constructor(
        private val repository: IGitHubRepository
) : IGitHubUseCases {

    override fun searchUsers(query: String) {
        repository
                .searchUsers(query)
    }

    override fun hasResults(query: String): Boolean {
        return repository.getQueryExist(query)
    }

    override fun getSearchUsersFlow(query: String): Flowable<List<GitHubUser>> {
        return repository.getSearchUsersFlow(query)
    }

    override fun hasMore(query: String): Boolean {
        return repository.getHasMore(query)
    }

}