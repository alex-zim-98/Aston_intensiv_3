package com.aston.myapplication.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import com.aston.myapplication.domain.entity.Contact

class ContactListItemCallbackDiffUtil: DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}