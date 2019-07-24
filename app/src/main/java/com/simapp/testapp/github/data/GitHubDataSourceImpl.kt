package com.simapp.testapp.github.data

import com.simapp.testapp.github.domain.GitHubUser
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

private interface IGithubApiService {
    @GET("search/users")
    fun search(@Query("q") query: String): Maybe<GithubSearchResult>
}

private data class GithubSearchResult(
        val items: List<GitHubUser>? = null
)

@Singleton
class GitHubDataSourceImpl @Inject constructor() : IGitHubDataSource {

    private val compositeDisposable = CompositeDisposable()

    private val apiService = Retrofit.Builder()
            .baseUrl(GITHUB_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(IGithubApiService::class.java)

    override fun loadUsers(query: String, processor: Subscriber<List<GitHubUser>>) {
        apiService
                .search(query)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { result ->
                            if (result.items != null) {
                                processor.onNext(result.items)
                            } else {
                                processor.onNext(listOf())
                            }
                        },
                        {
                            processor.onNext(listOf())
                        }
                )
                .also {
                    compositeDisposable.add(it)
                }
    }


    companion object {
        private const val GITHUB_URL = "https://api.github.com/"
    }
}