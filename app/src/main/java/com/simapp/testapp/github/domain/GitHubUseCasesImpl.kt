package com.simapp.testapp.github.domain

import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

@Singleton
class GitHubUseCasesImpl @Inject constructor(
        private val repository: IGitHubRepository
): IGitHubUseCases {

    override fun searchUsers(query: String, offset: Int, len: Int): Maybe<List<GitHubUser>> {
        return repository
                .searchUsers(query)
                .map { result ->
                    if (result.isEmpty() || offset > result.size - 1) listOf() else
                        result.subList(offset, min(offset + len, result.size -1))
                }
    }

    override fun hasMore(query: String, offset: Int): Boolean {
        return repository.getSearchResultSize(query) > offset
    }

}