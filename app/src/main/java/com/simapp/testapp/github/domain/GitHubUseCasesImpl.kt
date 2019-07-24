package com.simapp.testapp.github.domain

import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

@Singleton
class GitHubUseCasesImpl @Inject constructor(
        private val repository: IGitHubRepository
): IGitHubUseCases {

    override fun searchUsers(query: String, start: Int, len: Int): Maybe<List<GitHubUser>> {
        return repository
                .searchUsers(query)
                .map { result ->
                    if (result.isEmpty() || start > result.size - 1) listOf() else
                        result.subList(start, min(start + len, result.size -1))
                }
    }

}