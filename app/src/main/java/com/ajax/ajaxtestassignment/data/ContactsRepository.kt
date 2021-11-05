package com.ajax.ajaxtestassignment.data

import com.ajax.ajaxtestassignment.api.contacts.ContactsService
import com.ajax.ajaxtestassignment.db.contacts.ContactsDao
import com.ajax.ajaxtestassignment.db.contacts.DbContact
import com.ajax.ajaxtestassignment.ui.contactslist.viewmodel.Contact
import com.ajax.ajaxtestassignment.ui.contactslist.viewmodel.OperationResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO Wrap data sources in DataSource classes
class ContactsRepository @Inject constructor(
    val contactsService: ContactsService,
    val contactsDao: ContactsDao
) {

    fun fetchContacts(load: Boolean): Flow<OperationResult<List<Contact>>> = flow {
        if (load) {
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

}