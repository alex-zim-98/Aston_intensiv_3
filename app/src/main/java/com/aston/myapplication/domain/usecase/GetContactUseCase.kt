package com.aston.myapplication.domain.usecase

import androidx.lifecycle.LiveData
import com.aston.myapplication.domain.entity.Contact
import com.aston.myapplication.domain.repository.ContactRepository

class GetContactUseCase(val repository: ContactRepository) {
    suspend operator fun invoke(contactId: Int): LiveData<Contact> {
        return repository.getContact(contactId)
    }
}