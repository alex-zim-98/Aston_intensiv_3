package com.aston.myapplication.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.aston.myapplication.R
import com.aston.myapplication.databinding.ItemContactBinding
import com.aston.myapplication.domain.entity.Contact
import com.aston.myapplication.presentation.ui.ContactListViewHolder

class ContactDelegateAdapter(
    private val clickOnItemListener: ((item: Contact) -> Unit)?,
    private val selectedMap: MutableMap<Int, Contact>,
    private val modeDelete: Boolean
) : DelegateAdapter<Contact, ContactListViewHolder> {

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

        holder.itemView.setOnClickListener {
            if (modeDelete) {
                if (selectedMap.contains(position)) {
                    selectedMap.remove(position)
                } else {
                    selectedMap[position] = item
                }
            } else {
                clickOnItemListener?.invoke(item)
            }
        }
    }
}

