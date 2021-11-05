package com.ajax.ajaxtestassignment.db.contacts

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {
    @Query("SELECT * FROM Contact")
    fun getContacts(): Flow<List<DbContact>>

    @Update
    suspend fun update(contact: DbContact)

    @Insert
    suspend fun addAll(contact: List<DbContact>)

    @Query("DELETE FROM Contact WHERE id = (:contactId)")
    suspend fun deleteById(contactId: Int)

    @Query("SELECT * FROM Contact WHERE id = (:contactId)")
    fun getById(contactId: Int): Flow<DbContact>

    @Query("DELETE FROM Contact")
    suspend fun deleteAll()
}