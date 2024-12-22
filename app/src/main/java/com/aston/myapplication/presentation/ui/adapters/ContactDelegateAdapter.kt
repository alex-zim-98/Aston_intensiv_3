package com.aston.myapplication.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aston.myapplication.R
import com.aston.myapplication.databinding.ItemContactBinding
import com.aston.myapplication.domain.entity.Contact
import com.aston.myapplication.presentation.ui.ContactListViewHolder

class ContactDelegateAdapter(
    private val clickOnItemListener: ((item: Contact) -> Unit)?,
    private val selectedMap: MutableMap<Int, Contact>,
    var modeDelete: Boolean
) : DelegateAdapter<Contact, ContactListViewHolder> {
    private var compositeAdapter: CompositeDelegateAdapter<Contact, RecyclerView.ViewHolder>? = null

    fun setCompositeAdapter(adapter: CompositeDelegateAdapter<Contact, RecyclerView.ViewHolder>) {
        compositeAdapter = adapter
    }

    override fun isForViewType(item: Contact, position: Int): Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup): ContactListViewHolder {
        val viewBinding = ItemContactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactListViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, item: Contact, position: Int) {
        holder.viewBinding.tvName.text = item.name
        holder.viewBinding.tvLastname.text = item.lastName
        holder.viewBinding.tvPhone.text = item.phone

        val color = if (selectedMap.contains(position)) R.color.gray else R.color.white
        val drawable = ContextCompat.getDrawable(holder.itemView.context, color)
        holder.itemView.background = drawable
        changeBackgroundView(holder, getColorBackground(holder.adapterPosition))

        holder.itemView.setOnClickListener {
            rightMode(holder.adapterPosition, item)
            compositeAdapter?.notifyItemChangedSafe(holder.adapterPosition)
        }

    }
    private fun rightMode(position: Int, contact: Contact) {
        if (modeDelete) {
            launchDeleteMode(position, contact)
        } else {
            clickOnItemListener?.invoke(contact)
        }
    }

    private fun launchDeleteMode(position: Int, contact: Contact) {
        if (selectedMap.contains(position)) {
            selectedMap.remove(position)
        } else {
            selectedMap[position] = contact
        }
    }

    private fun getColorBackground(position: Int): Int {
        return if (selectedMap.contains(position)) R.color.gray else R.color.white
    }

    private fun changeBackgroundView(
        holder: ContactListViewHolder,
        color: Int
    ) {
        val drawable = ContextCompat.getDrawable(holder.itemView.context, color)
        holder.itemView.background = drawable
    }

}

