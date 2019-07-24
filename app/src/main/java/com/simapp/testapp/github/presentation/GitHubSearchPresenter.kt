package com.simapp.testapp.github.presentation

import android.util.Log
import com.simapp.clean.base.presentation.BaseCleanPresenter
import com.simapp.testapp.auth.domain.IAuthUseCases
import com.simapp.testapp.github.domain.GitHubUser
import com.simapp.testapp.github.domain.IGitHubUseCases
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.processors.PublishProcessor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GitHubSearchPresenter @Inject constructor(
        private val gitHubUseCases: IGitHubUseCases,
        private val authUseCases: IAuthUseCases
) : BaseCleanPresenter<IContract.IGitHubSearchView>(), IContract.IGitHubSearchPresenter {

    private val queryTextProcessor = PublishProcessor.create<String>()

    private var resultSearchDisposable = Disposables.disposed()

    private var currentQuery = ""

    init {
        handleOnQueryTextChange()
    }

    override fun onStart() {
        super.onStart()
        view?.setVisibleNextButton(gitHubUseCases.hasMore(currentQuery))
        authUseCases
                .getUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    it.user?.also { user ->
                        view?.setCurrentUser(user)
                    }
                }
                .also {
                    onStartViewSubscription.add(it)
                }

        subscribeOnQuery()
    }

    override fun onExitClicked() {
        authUseCases.removeAuthData()
    }

    private fun subscribeOnQuery() {
        resultSearchDisposable.dispose()
        resultSearchDisposable = gitHubUseCases
                .getSearchUsersFlow(currentQuery)
                .map {
                    it.takeLast(LIST_SIZE)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            handleSearchResult(it)
                        },
                        { }
                )
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
                .map { strings ->
                    strings.last()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { query ->
                    if (!gitHubUseCases.hasResults(query)) {
                        gitHubUseCases.searchUsers(query)
                    }
                    subscribeOnQuery()
                }
                .also { onCreatePresenterSubscription.add(it) }
    }

    private fun handleSearchResult(result: List<GitHubUser>) {
        view?.setVisibleNextButton(gitHubUseCases.hasMore(currentQuery))
        view?.submitList(result)
    }

    override fun nextPage() {
        gitHubUseCases.searchUsers(currentQuery)
    }


    companion object {
        private const val BUFFER_TIME = 600L
        private const val LIST_SIZE = 30
    }

}