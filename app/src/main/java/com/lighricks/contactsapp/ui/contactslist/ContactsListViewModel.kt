package com.lighricks.contactsapp.ui.contactslist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactsListViewModel @Inject constructor() : ViewModel() {

    fun getContacts(): List<Contact> = createContacts()

    private fun createContacts(): List<Contact> = (0..10).map {
        Contact(id = it.toString(), name = "Contact $it")
    }
}

data class Contact(
    val id: String,
    val name: String
)
