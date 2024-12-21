package com.aston.myapplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aston.myapplication.data.ContactRepositoryImpl
import com.aston.myapplication.domain.entity.Contact
import com.aston.myapplication.domain.usecase.AddContactUseCase
import com.aston.myapplication.domain.usecase.ChangeContactUseCase
import com.aston.myapplication.domain.usecase.GetContactUseCase
import kotlinx.coroutines.launch

class NewContactViewModel : ViewModel() {
    private val repository = ContactRepositoryImpl
    private val addContactUseCase = AddContactUseCase(repository)
    private val getContactUseCase = GetContactUseCase(repository)
    private val changeContactUseCase = ChangeContactUseCase(repository)

    private val _contactItem = MutableLiveData<Contact>()
    val contactItem: LiveData<Contact> get() = _contactItem

    private val _shouldClosed = MutableLiveData<Unit>()
    val shouldClosed: LiveData<Unit> get() = _shouldClosed

    private val _errorInputName = MutableLiveData(false)
    val errorInputName: LiveData<Boolean> get() = _errorInputName

    private val _errorInputLastName = MutableLiveData(false)
    val errorInputLastName: LiveData<Boolean> get() = _errorInputLastName

    private val _errorInputPhone = MutableLiveData(false)
    val errorInputPhone: LiveData<Boolean> get() = _errorInputPhone

    fun getContactById(contactId: Int) {
        viewModelScope.launch {
            val item = getContactUseCase(contactId).value
            item?.let {
                _contactItem.value = it
            }
        }
    }

    fun addNewContact(inputName: String?, inputLastName: String?, inputPhone: String?) {
        checkValidContact(inputName, inputLastName, inputPhone) { name, lastName, phone ->
            val contact = Contact(name, lastName, phone)
            viewModelScope.launch {
                addContactUseCase(contact)
                _shouldClosed.postValue(Unit)
            }
        }
    }

    fun editExistContact(
        inputName: String?,
        inputLastName: String?,
        inputPhone: String?
    ) {
        checkValidContact(inputName, inputLastName, inputPhone) { name, lastName, phone ->
            viewModelScope.launch {
                val item = _contactItem.value
                item?.let {
                    changeContactUseCase.invoke(
                        it.copy(
                            name = name,
                            lastName = lastName,
                            phone = phone
                        )
                    )
                    _shouldClosed.postValue(Unit)
                }
            }
        }
    }

    private fun checkValidContact(
        inputName: String?,
        inputLastName: String?,
        inputPhone: String?,
        action: (name: String, lastName: String, phone: String) -> Unit
    ) {
        val name = parseField(inputName)
        val lastName = parseField(inputLastName)
        val phone = parseField(inputPhone)

        if (!invalidContact(name, lastName, phone)) {
            action.invoke(name, lastName, phone)
        }
    }

    private fun parseField(field: String?): String {
        return field?.trim() ?: throw RuntimeException("Incorrect field!")
    }

    private fun invalidContact(name: String, lastName: String, phone: String): Boolean {
        var result = false
        if (name.length <= 3) {
            result = true
            _errorInputName.value = true
        }
        if (lastName.length < 3) {
            result = true
            _errorInputLastName.value = true
        }

        if (phone.length <= 5) {
            result = true
            _errorInputPhone.value = true
        }
        return result
    }

    fun resetErrorName() {
        _errorInputName.value = false
    }

    fun resetErrorLastName() {
        _errorInputLastName.value = false
    }

    fun resetErrorPhone() {
        _errorInputPhone.value = false
    }
}