package com.lighricks.contactsapp.data

import android.Manifest
import androidx.annotation.RequiresPermission
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepo @Inject constructor(
    private val contactsChangedObserver: ContactsChangedObserver,
    private val contactsStorage: ContactsStorage
) {

    @RequiresPermission(Manifest.permission.READ_CONTACTS)
    fun observeDeviceContacts() {
        contactsChangedObserver.observe()
    }

    fun getContacts() = contactsStorage.getContacts()

    fun getContact(id: Long) = contactsStorage.getContact(id)
}
