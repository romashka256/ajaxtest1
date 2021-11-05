package com.ajax.ajaxtestassignment.data

import com.ajax.ajaxtestassignment.api.contacts.ContactsService
import com.ajax.ajaxtestassignment.common.OperationResult
import com.ajax.ajaxtestassignment.db.contacts.ContactsDao
import com.ajax.ajaxtestassignment.db.contacts.DbContact
import com.ajax.ajaxtestassignment.domain.entity.Contact
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO Wrap data sources in DataSource classes
class ContactsRepository @Inject constructor(
    val contactsService: ContactsService,
    val contactsDao: ContactsDao
) {

    suspend fun loadContacts() {
        contactsService.getContacts(limit = 30).results
            ?.map {
                DbContact(
                    firstName = it.name?.firstName ?: "",
                    lastName = it.name?.lastName ?: "",
                    email = it.email,
                    photo = it.picture?.medium ?: "",
                )
            }.also {
                contactsDao.deleteAll()
                contactsDao.addAll(it ?: emptyList())
            }
    }


    fun fetchContacts(load: Boolean): Flow<OperationResult<List<Contact>>> = flow {
        if (load) {
            loadContacts()
        }

        emitAll(
            contactsDao.getContacts()
                .map { list ->
                    OperationResult.Success(list.map {
                        Contact(
                            id = it.id,
                            firstName = it.firstName ?: "",
                            lastName = it.lastName ?: "",
                            email = it.email,
                            photo = it.photo ?: "",
                        )
                    })
                }
        )
    }

    fun getContact(id: Int): Flow<OperationResult<Contact>> = flow {
        emitAll(contactsDao.getById(id).map {
            OperationResult.Success(
                Contact(
                    id = it.id,
                    firstName = it.firstName ?: "",
                    lastName = it.lastName ?: "",
                    email = it.email,
                    photo = it.photo ?: "",
                )
            )
        })
    }


    suspend fun delete(id: Int) {
        contactsDao.deleteById(id)
    }
}