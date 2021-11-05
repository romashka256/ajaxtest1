package com.ajax.ajaxtestassignment.ui.details.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajax.ajaxtestassignment.common.OperationResult
import com.ajax.ajaxtestassignment.data.ContactsRepository
import com.ajax.ajaxtestassignment.domain.entity.Contact
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class ContactDetailsViewModel @Inject constructor(
    val contactsRepository: ContactsRepository
) : ViewModel() {

    fun contactUpdates(id: Int): StateFlow<OperationResult<Contact>> {
       return loadContact(id)
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5000),
                initialValue = OperationResult.Loading
            )
    }

    private fun loadContact(id: Int): Flow<OperationResult<Contact>> {
        return contactsRepository.getContact(id)
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            contactsRepository.delete(id)
        }
    }
}