package com.lighricks.contactsapp.ui.contactslist

import androidx.lifecycle.ViewModel
import com.lighricks.contactsapp.data.ContactData
import com.lighricks.contactsapp.data.ContactsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ContactsListViewModel @Inject constructor(
    private val contactsRepo: ContactsRepo
) : ViewModel() {

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
