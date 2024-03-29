package com.simapp.testapp.auth.presentation

import android.os.Bundle
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.simapp.base.presentation.DaggerBaseCleanFragment
import com.simapp.testapp.R
import com.simapp.testapp.auth.domain.AuthTypes
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.android.synthetic.main.auth_list_item.view.*

@Module
abstract class AuthFragmentModule {
    @ContributesAndroidInjector(modules = [AuthFragmentPresenterModule::class])
    abstract fun getFragment(): AuthFragment
}

class AuthFragment : DaggerBaseCleanFragment<IContract.IAuthView, IContract.IPresenter>(), IContract.IAuthView {

    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = Adapter {
            presenter?.onItemClick(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.auth_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.list)?.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun submitAuthList(list: List<AuthListItem>) {
        adapter.submitList(list)
    }
}

class ViewHolder(itemView: View, private val onClick: (AuthTypes) -> Unit) : RecyclerView.ViewHolder(itemView) {
    fun bind(data: AuthListItem?) {
        data ?: return
        itemView.text.text = data.name
        itemView.setOnClickListener {
            onClick(data.type)
        }
    }
}

class Adapter(private val onClick: (AuthTypes) -> Unit) : ListAdapter<AuthListItem, ViewHolder>(object : DiffUtil.ItemCallback<AuthListItem>() {
    override fun areItemsTheSame(oldItem: AuthListItem?, newItem: AuthListItem?) = oldItem == newItem
    override fun areContentsTheSame(oldItem: AuthListItem?, newItem: AuthListItem?) = oldItem == newItem
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.auth_list_item, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
