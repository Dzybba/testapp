package com.simapp.testapp.github.data

import com.simapp.testapp.github.domain.GitHubUser
import org.reactivestreams.Subscriber

interface IGitHubDataSource {
    fun loadUsers(query: String, processor: Subscriber<List<GitHubUser>>)
}