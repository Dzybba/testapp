package com.simapp.testapp.github.presentation

import android.os.Bundle
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.simapp.clean.base.presentation.DaggerBaseCleanFragment
import com.simapp.testapp.R
import com.simapp.testapp.auth.domain.User
import com.simapp.testapp.github.domain.GitHubUser
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import kotlinx.android.synthetic.main.github_search_fragment_layout.*
import kotlinx.android.synthetic.main.github_user_list_item.view.*

@Module
abstract class GitHubSearchFragmentModule {
    @ContributesAndroidInjector
    abstract fun getFragment(): GitHubSearchFragment
}

class GitHubSearchFragment: DaggerBaseCleanFragment<IContract.IGitHubSearchView, GitHubSearchPresenter>(), IContract.IGitHubSearchView {

    private lateinit var adapter: Adapter

    private val exitProcessor = PublishProcessor.create<Unit>()
    val exitFlow: Flowable<Unit>
        get() = exitProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = Adapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.github_search_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        list.layoutManager = layoutManager

        search_input.hint = "Search"
        search_input?.setText(presenter?.getCurrentQuery() ?: "")

        search_input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && search_input != null) {
                    presenter?.onSearchQuery(search_input.text.toString())
                }
            }
        })
        next_button.setOnClickListener {
            presenter?.nextPage()
        }
        exit_button.setOnClickListener {
            exitProcessor.onNext(Unit)
            exitProcessor.onComplete()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exitProcessor.onComplete()
    }

    override fun submitList(list: List<GitHubUser>) {
        adapter.submitList(null)
        adapter.submitList(list)
    }

    override fun setVisibleNextButton(visible: Boolean) {
        next_button.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun setCurrentUser(currentUser: User) {
        Picasso.with(context).load(currentUser.photoUrl).into(avatar)
        user_nick.text = currentUser.name
    }

}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(data: GitHubUser?) {
        data ?: return
        itemView.text.text = data.login
        Picasso.with(itemView.context).load(data.avatar_url).into(itemView.avatar)
    }
    fun onStopUsing() {
        Picasso.with(itemView.context).cancelRequest(itemView.avatar)
    }
}


class Adapter : ListAdapter<GitHubUser, ViewHolder>(object : DiffUtil.ItemCallback<GitHubUser>() {
    override fun areItemsTheSame(oldItem: GitHubUser?, newItem: GitHubUser?) = oldItem == newItem
    override fun areContentsTheSame(oldItem: GitHubUser?, newItem: GitHubUser?) = oldItem == newItem
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.github_user_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.onStopUsing()
    }
}
