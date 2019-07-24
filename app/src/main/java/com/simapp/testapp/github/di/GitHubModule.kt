package com.simapp.testapp.github.di

import com.simapp.testapp.github.data.GitHubDataSourceImpl
import com.simapp.testapp.github.data.GitHubRepositoryImpl
import com.simapp.testapp.github.data.IGitHubDataSource
import com.simapp.testapp.github.domain.GitHubUseCasesImpl
import com.simapp.testapp.github.domain.IGitHubRepository
import com.simapp.testapp.github.domain.IGitHubUseCases
import dagger.Binds
import dagger.Module

@Module
abstract class GitHubModule {
    @Binds
    abstract fun getUseCases(useCases: GitHubUseCasesImpl): IGitHubUseCases

    @Binds
    abstract fun getRepository(rep: GitHubRepositoryImpl): IGitHubRepository

    @Binds
    abstract fun getDataSource(ds: GitHubDataSourceImpl): IGitHubDataSource
}