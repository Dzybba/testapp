package com.simapp.testapp.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.simapp.base.presentation.DaggerBaseCleanFragment
import com.simapp.testapp.R
import com.simapp.testapp.auth.presentation.AuthFragment
import com.simapp.testapp.github.presentation.GitHubSearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentModule {
    @ContributesAndroidInjector(modules = [MainFragmentPresenterModule::class])
    abstract fun getFragment(): MainFragment
}

class MainFragment: DaggerBaseCleanFragment<IContract.IMainView, IContract.IMainViewPresenter>(), IContract.IMainView {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter?.onViewCreated()
    }

    override fun showAuthView() {
        childFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_area, getAuthFragment(), AUTH_FRAGMENT_TAG)
                .commit()
    }

    override fun showGitHubView() {
        childFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_area, getGitHubFragment(), GITHUB_FRAGMENT_TAG)
                .commit()
    }

    private fun getAuthFragment(): AuthFragment {
        return (childFragmentManager.findFragmentByTag(AUTH_FRAGMENT_TAG) as? AuthFragment) ?: AuthFragment()
    }

    private fun getGitHubFragment(): GitHubSearchFragment {
        return (childFragmentManager.findFragmentByTag(GITHUB_FRAGMENT_TAG) as? GitHubSearchFragment) ?: GitHubSearchFragment()
    }

    companion object {
        private const val AUTH_FRAGMENT_TAG = "AUTH_FRAGMENT_TAG"
        private const val GITHUB_FRAGMENT_TAG = "GITHUB_FRAGMENT_TAG"
    }

}