package com.ajax.ajaxtestassignment.ui.contactslist.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajax.ajaxtestassignment.data.ContactsRepository
import com.ajax.ajaxtestassignment.domain.entity.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class ContactListViewModel @Inject constructor(
    val contactsRepository: ContactsRepository,
    val preferences: SharedPreferences
) : ViewModel() {

    val contactsUpdates: StateFlow<OperationResult<List<Contact>>> = loadContacts()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = OperationResult.Loading
        )

    private fun loadContacts(): Flow<OperationResult<List<Contact>>> {
        return contactsRepository.fetchContacts(checkFirstRun())
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            contactsRepository.delete(id)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            contactsRepository.loadContacts()
        }
    }

    fun checkFirstRun(): Boolean {
        return if (preferences.getBoolean("first_run", true)) {
            preferences.edit().putBoolean("first_run", false).apply();
            true;
        } else {
            false;
        }
    }
}


sealed class OperationResult<out T> {
    object Loading : OperationResult<Nothing>()
    data class Success<out T>(val value: T) : OperationResult<T>()
    data class Failed(val value: Exception) : OperationResult<Nothing>()
}