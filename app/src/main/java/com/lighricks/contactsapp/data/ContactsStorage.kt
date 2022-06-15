package com.lighricks.contactsapp.data

import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsStorage @Inject constructor() {

    private val contacts = MutableStateFlow<Map<Long, ContactData>>(emptyMap())

    fun updateContacts(newContacts: List<ContactData>) {
        contacts.value = newContacts.associateBy { it.id }
    }

    fun getContacts(): Flow<Collection<ContactData>> = contacts.map { it.values }

    fun getContact(id: Long): Flow<ContactData> {
        return contacts.mapNotNull { it[id] }
    }
}
