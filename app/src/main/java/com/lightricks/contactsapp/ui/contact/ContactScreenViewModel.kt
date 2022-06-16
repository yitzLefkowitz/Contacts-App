package com.lightricks.contactsapp.ui.contact

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.lightricks.contactsapp.data.ContactsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ContactScreenViewModel @Inject constructor(
    private val contactsRepo: ContactsRepo
) : ViewModel() {

    fun getContactData(id: Long): Flow<ContactUiData> {
        return contactsRepo.getContact(id).map {
            ContactUiData(
                id = it.id,
                name = it.name,
                email = it.email,
                phone = it.phone,
                image = it.image
            )
        }
    }
}

data class ContactUiData(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String,
    val image: Uri?
) {
    val showEmail = email.isNotEmpty()
    val showPhone = phone.isNotEmpty()
}
