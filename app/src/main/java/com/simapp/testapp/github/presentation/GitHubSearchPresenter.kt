package com.simapp.testapp.github.presentation

import com.simapp.clean.base.presentation.BaseCleanPresenter
import com.simapp.testapp.github.domain.GitHubUser
import com.simapp.testapp.github.domain.IGitHubUseCases
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GitHubSearchPresenter @Inject constructor(
        private val gitHubUseCases: IGitHubUseCases
) : BaseCleanPresenter<IContract.IGitHubSearchView>(), IContract.IGitHubSearchPresenter {

    private val queryTextProcessor = PublishProcessor.create<String>()

    private var currentQuery = ""
    private var currentOffset = 0

    init {
        handleOnQueryTextChange()
    }

    override fun onStart() {
        super.onStart()
        view?.setVisibleNextButton(gitHubUseCases.hasMore(currentQuery, currentOffset))
    }

    override fun onSearchQuery(query: String) {
        if (currentQuery == query) return
        currentQuery = query
        queryTextProcessor.onNext(query)
    }

    override fun getCurrentQuery(): String {
        return currentQuery
    }

    private fun handleOnQueryTextChange() {
        queryTextProcessor
                .buffer(BUFFER_TIME, TimeUnit.MILLISECONDS)
                .filter { strings -> strings.isNotEmpty() }
                .flatMapMaybe { strings ->
                    currentOffset = 0
                    gitHubUseCases.searchUsers(strings.last(), currentOffset, LIST_SIZE)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    handleSearchResult(result)
                }
                .also { onCreatePresenterSubscription.add(it) }
    }

    private fun handleSearchResult(result: List<GitHubUser>) {
        currentOffset = result.size
        view?.setVisibleNextButton(gitHubUseCases.hasMore(currentQuery, currentOffset))
        view?.submitList(result)
    }

    override fun nextPage() {
        gitHubUseCases
                .searchUsers(currentQuery, currentOffset, LIST_SIZE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            handleSearchResult(it)
                        },
                        {

                        })
                .also { onStartViewSubscription.add(it) }

    }


    companion object {
        private const val BUFFER_TIME = 600L
        private const val LIST_SIZE = 30
    }

}