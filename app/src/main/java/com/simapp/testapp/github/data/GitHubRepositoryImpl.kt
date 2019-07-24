package com.simapp.testapp.github.data

import com.simapp.testapp.github.domain.GitHubUser
import com.simapp.testapp.github.domain.IGitHubRepository
import io.reactivex.Maybe
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubRepositoryImpl @Inject constructor(
        private val dataSource: IGitHubDataSource
) : IGitHubRepository {

    private val resultsMap = hashMapOf<String, BehaviorProcessor<List<GitHubUser>>>()

    override fun searchUsers(query: String): Maybe<List<GitHubUser>> {
        if (query.isEmpty()) return Maybe.just(listOf())
        return resultsMap.getOrPut(query) {
            BehaviorProcessor.create<List<GitHubUser>>().apply {
                dataSource.loadUsers(query, this)
            }
        }.firstElement()
    }

    override fun getSearchResultSize(query: String): Int {
        return resultsMap[query]?.value?.size ?: 0
    }

}