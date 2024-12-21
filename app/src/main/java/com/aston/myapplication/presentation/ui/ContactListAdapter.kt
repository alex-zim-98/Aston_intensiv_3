package com.aston.myapplication.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.aston.myapplication.R
import com.aston.myapplication.databinding.ItemContactBinding
import com.aston.myapplication.domain.entity.Contact
import com.aston.myapplication.presentation.utils.ContactListItemCallbackDiffUtil

class ContactListAdapter :
    ListAdapter<Contact, ContactListViewHolder>(ContactListItemCallbackDiffUtil()) {
    var clickOnItemListener: ((item: Contact) -> Unit)? = null
    var selectedMap = mutableMapOf<Int, Contact>()
    var modeDelete = false
        set(value) {
            if (!value) {
                for (pos in selectedMap.keys) {
                    notifyItemChanged(pos)
                }
                selectedMap.clear()
            }

            field = value
        }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val updatedList = currentList.toMutableList()

        val movedItem = updatedList.removeAt(fromPosition)
        updatedList.add(toPosition, movedItem)

        submitList(updatedList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        val viewBinding = ItemContactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactListViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        val contact = getItem(holder.adapterPosition)
        holder.viewBinding.tvName.text = contact.name
        holder.viewBinding.tvLastname.text = contact.lastName
        holder.viewBinding.tvPhone.text = contact.phone

        changeBackgroundView(holder, getColorBackground(holder.adapterPosition))

        holder.itemView.setOnClickListener {
            rightMode(holder.adapterPosition, contact)
            notifyItemChanged(holder.adapterPosition)
        }
    }

    private fun rightMode(position: Int, contact: Contact) {
        if (modeDelete) {
            launchDeleteMode(position, contact)
        } else {
            clickOnItemListener?.invoke(contact)
        }
    }

    private fun launchDeleteMode(
        position: Int,
        contact: Contact
    ) {
        if (selectedMap.contains(position)) {
            selectedMap.remove(position)
        } else {
            selectedMap.put(position, contact)
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