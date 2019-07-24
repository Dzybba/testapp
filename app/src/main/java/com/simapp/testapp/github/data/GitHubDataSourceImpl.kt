package com.simapp.testapp.github.data

import com.simapp.testapp.github.domain.GithubSearchResult
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

private interface IGithubApiService {
    @GET("search/users")
    fun search(
            @Query("q") query: String,
            @Query("page") page: String,
            @Query("per_page") perPage: String
    ): Maybe<GithubSearchResult>
}

@Singleton
class GitHubDataSourceImpl @Inject constructor() : IGitHubDataSource {

    private val apiService = Retrofit.Builder()
            .baseUrl(GITHUB_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(IGithubApiService::class.java)

    override fun loadUsers(query: String, page: Int, perPage: Int): Maybe<GithubSearchResult> {
        return apiService
                .search(query, page.toString(), perPage.toString())
                .subscribeOn(Schedulers.io())
    }

    companion object {
        private const val GITHUB_URL = "https://api.github.com/"
    }
}