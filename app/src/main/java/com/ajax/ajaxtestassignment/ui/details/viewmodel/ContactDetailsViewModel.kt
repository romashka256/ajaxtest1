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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class ContactDetailsViewModel @Inject constructor(
    val contactsRepository: ContactsRepository
) : ViewModel() {

    private lateinit var contactUpdates: StateFlow<OperationResult<Contact>>
    fun contactUpdates(id: Int): StateFlow<OperationResult<Contact>> {
        contactUpdates = loadContact(id)
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5000),
                initialValue = OperationResult.Loading
            )

        return contactUpdates
    }

    private fun loadContact(id: Int): Flow<OperationResult<Contact>> {
        return contactsRepository.getContact(id)
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            contactsRepository.delete(id)
        }
    }

    fun save(name: String, lastName: String) {
        viewModelScope.launch {
            val contact = contactUpdates.first()

            if(contact is OperationResult.Success) {
                contactsRepository.save(
                    contact.value.copy(
                        firstName = name,
                        lastName = lastName
                    )
                )
            }
        }
    }
}
