package com.aston.myapplication.domain.usecase

import com.aston.myapplication.domain.entity.Contact
import com.aston.myapplication.domain.repository.ContactRepository

class ChangeContactUseCase(val repository: ContactRepository) {
    suspend operator fun invoke(contact: Contact) {
        repository.changeContact(contact)
    }
}