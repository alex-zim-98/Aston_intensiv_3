package com.aston.myapplication.presentation.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aston.myapplication.presentation.ui.ContactListViewHolder

class CompositeDelegateAdapter<T>(
    private val delegates: List<DelegateAdapter<T, ContactListViewHolder>>
) : RecyclerView.Adapter<ContactListViewHolder>() {

    val items = mutableListOf<T>()

    fun submitList(newItems: List<T>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return delegates.indexOfFirst { it.isForViewType(items[position], position) }
            .takeIf { it != -1 }
            ?: throw IllegalArgumentException("No delegate found for position $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        return delegates[viewType].onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        val delegate = delegates[getItemViewType(position)]
        delegate.onBindViewHolder(holder, items[position], position)
    }

    override fun getItemCount(): Int = items.size
}

