package com.aston.myapplication.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aston.myapplication.domain.entity.Contact
import com.aston.myapplication.domain.repository.ContactRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.TreeSet

object ContactRepositoryImpl: ContactRepository {
    private val listContactLD = MutableLiveData<List<Contact>>()
    private val contactLD = MutableLiveData<Contact>()

    private val listContact = TreeSet(Comparator<Contact> { contactA, contactB ->
        contactA.id.compareTo(contactB.id) })

    private var autoincrement = 0

    init {
        CoroutineScope(Dispatchers.Main).launch {
            for (i in 1..100) {
                addContact(
                    Contact(
                        "Name $i",
                        "Lastname $i",
                        "+7(951) $i")
                )
            }
        }
    }

    override suspend fun addContact(contact: Contact) {
        if (contact.id == Contact.UNDEFINED_ID) {
            contact.id = ++autoincrement
        }
        listContact.add(contact)
        updateData()
    }

    override suspend fun removeContact(contact: Contact) {
        listContact.remove(contact)
        updateData()
    }

    override suspend fun changeContact(contact: Contact) {
        removeContact(contact)
        listContact.add(contact)
        updateData()
    }

    override suspend fun getContact(contactId: Int): LiveData<Contact> {
        val contact = listContact.find { it.id == contactId } ?:
            throw RuntimeException("Undefined id")
        contactLD.value = contact
        return contactLD
    }

    override suspend fun getContactList(): LiveData<List<Contact>> {
        updateData()
        return listContactLD
    }

    private fun updateData() {
        listContactLD.value = listContact.toList()
    }
}