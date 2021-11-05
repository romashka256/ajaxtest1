package com.ajax.ajaxtestassignment.ui.contactslist.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.ajax.ajaxtestassignment.data.ContactsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
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

    fun checkFirstRun(): Boolean {
        return if (preferences.getBoolean("first_run", true)) {
            preferences.edit().putBoolean("first_run", false).apply();
            true;
        } else {
            false;
        }
    }
}


data class Contact(
    val id: Int,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val photo: String?
)

sealed class OperationResult<out T> {
    object Loading : OperationResult<Nothing>()
    data class Success<out T>(val value: T) : OperationResult<T>()
    data class Failed(val value: Exception) : OperationResult<Nothing>()
}