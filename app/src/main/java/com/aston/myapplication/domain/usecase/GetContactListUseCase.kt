package com.aston.myapplication.domain.usecase

import androidx.lifecycle.LiveData
import com.aston.myapplication.domain.entity.Contact
import com.aston.myapplication.domain.repository.ContactRepository

class GetContactListUseCase(val repository: ContactRepository) {
    suspend operator fun invoke(): LiveData<List<Contact>> {
        return repository.getContactList()
    }
}