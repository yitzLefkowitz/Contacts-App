package com.lightricks.contactsapp.ui.contactslist

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import com.lightricks.contactsapp.data.ContactData
import com.lightricks.contactsapp.data.ContactsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ContactsListViewModel @Inject constructor(
    private val contactsRepo: ContactsRepo
) : ViewModel() {

    @RequiresPermission(Manifest.permission.READ_CONTACTS)
    fun observeContacts() = contactsRepo.observeDeviceContacts()

    fun getContacts(): Flow<List<ContactRow>> {
        return contactsRepo.getContacts().map {
            it.map { contactData -> contactData.toContactRow() }
        }
    }

    private fun ContactData.toContactRow(): ContactRow {
        return ContactRow(
            this.id,
            this.name
        )
    }
}

data class ContactRow(
    val id: Long,
    val name: String
)
