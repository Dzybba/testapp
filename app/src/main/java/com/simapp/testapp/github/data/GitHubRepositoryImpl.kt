package com.simapp.testapp.github.data

import android.util.Log
import com.simapp.testapp.github.domain.GitHubUser
import com.simapp.testapp.github.domain.GithubSearchResult
import com.simapp.testapp.github.domain.IGitHubRepository
import io.reactivex.Flowable
import io.reactivex.disposables.Disposables
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubRepositoryImpl @Inject constructor(
        private val dataSource: IGitHubDataSource
) : IGitHubRepository {
    private val resultsMap = hashMapOf<String, BehaviorProcessor<GithubSearchResult>>()

    private var requestDisposable = Disposables.disposed()

    override fun searchUsers(query: String) {
        requestDisposable.dispose()
        val processor = getProcessor(query)
        val currentResult = processor.value ?: GithubSearchResult()
        val currentCount = currentResult.items.size
        val page = if (currentCount == 0) 0 else currentCount / ITEMS_PER_PAGE + 1
        requestDisposable = dataSource
                .loadUsers(query, page, ITEMS_PER_PAGE)
                .subscribe(
                        { result ->
                            Log.e("DD", "Get result ${result.total_count} ${result.items.size} $currentCount")
                            processor.onNext(GithubSearchResult(result.total_count, currentResult.items + result.items))
                        },
                        {}
                )
    }

    override fun getSearchUsersFlow(query: String): Flowable<List<GitHubUser>> {
        if (query.isEmpty()) return Flowable.just(listOf())

        return getProcessor(query)
                .map { it.items }
    }

    override fun getQueryExist(query: String): Boolean {
        return resultsMap.containsKey(query)
    }

    override fun getHasMore(query: String): Boolean {
        val value = resultsMap[query]?.value ?: return false
        return value.total_count > value.items.size
    }

    private fun getProcessor(query: String): BehaviorProcessor<GithubSearchResult> {
        return resultsMap.getOrPut(query) {
            BehaviorProcessor.create()
        }
    }

    companion object {
        private const val ITEMS_PER_PAGE = 30
    }

}