package com.aston.myapplication.domain.repository

import androidx.lifecycle.LiveData
import com.aston.myapplication.domain.entity.Contact

interface ContactRepository {
    suspend fun addContact(contact: Contact)
    suspend fun removeContact(contact: Contact)
    suspend fun changeContact(contact: Contact)
    suspend fun getContact(contactId: Int): LiveData<Contact>
    suspend fun getContactList(): LiveData<List<Contact>>
}