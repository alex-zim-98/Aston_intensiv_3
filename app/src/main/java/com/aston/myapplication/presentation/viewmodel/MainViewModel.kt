package com.aston.myapplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.aston.myapplication.data.ContactRepositoryImpl
import com.aston.myapplication.domain.entity.Contact
import com.aston.myapplication.domain.usecase.GetContactListUseCase
import com.aston.myapplication.domain.usecase.RemoveContactUseCase
import com.aston.myapplication.presentation.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _shouldCloseMode = SingleLiveEvent<Unit>()
    val shouldCloseMode: LiveData<Unit> get() = _shouldCloseMode

    private val repository = ContactRepositoryImpl
    private val getContactListUseCase = GetContactListUseCase(repository)

    private val removeContactUseCase = RemoveContactUseCase(repository)

    val listContact: LiveData<List<Contact>> = liveData {
        emitSource(getContactListUseCase.invoke())
    }

    fun removeListContact(listContact: List<Contact>) {
        viewModelScope.launch {
            for (contact in listContact) {
                removeContactUseCase.invoke(contact)
            }
            _shouldCloseMode.postValue(Unit)
        }
    }
}