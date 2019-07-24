package com.simapp.testapp.github.domain

data class GitHubUser(
        val login: String = "",
        val avatar_url: String = ""
)

data class GithubSearchResult(
        val total_count: Int = 0,
        val items: List<GitHubUser> = listOf()
)