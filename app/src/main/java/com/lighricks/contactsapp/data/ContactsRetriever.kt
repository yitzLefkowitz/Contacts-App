package com.lighricks.contactsapp.data

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.LongSparseArray
import android.util.Patterns
import androidx.core.content.ContextCompat
import androidx.core.util.valueIterator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

object ContactsRetriever {

    private val COLUMNS = arrayOf(
        ContactsContract.Data.CONTACT_ID,
        ContactsContract.Data.DISPLAY_NAME_PRIMARY,
        ContactsContract.Data.PHOTO_URI,
        ContactsContract.Data.PHOTO_THUMBNAIL_URI,
        ContactsContract.Data.DATA1,
        ContactsContract.Data.MIMETYPE
    )

    suspend fun getDeviceContacts(context: Context): List<ContactData> {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return emptyList()
        }

        return withContext(Dispatchers.IO) {
            val cursor = context.contentResolver.createCursor()
            cursor.toContactData().also {
                cursor?.close()
            }
        }
    }

    private fun ContentResolver.createCursor(): Cursor? {
        return this.query(
            ContactsContract.Data.CONTENT_URI,
            COLUMNS,
            null,
            null,
            ContactsContract.Data.CONTACT_ID
        )
    }

    private fun Cursor?.toContactData(): List<ContactData> {
        if (this == null) return emptyList()

        val contacts = LongSparseArray<ContactData>()

        val idIndex: Int = getColumnIndex(ContactsContract.Data.CONTACT_ID)
        val displayNamePrimaryIndex: Int = getColumnIndex(ContactsContract.Data.DISPLAY_NAME_PRIMARY)
        val imageIndex: Int = getColumnIndex(ContactsContract.Data.PHOTO_URI)
        val thumbnailIndex: Int = getColumnIndex(ContactsContract.Data.PHOTO_THUMBNAIL_URI)
        val mimeTypeIndex: Int = getColumnIndex(ContactsContract.Data.MIMETYPE)
        val data1Index: Int = getColumnIndex(ContactsContract.Data.DATA1)

        moveToFirst()
        while (!isAfterLast) {
            val id = getLong(idIndex)

            var contact = contacts.get(id, null)
            if (contact == null) {
                val displayName = getString(displayNamePrimaryIndex).orEmpty()
                val image = try {
                    Uri.parse(getString(imageIndex))
                } catch (e: Exception) {
                    null
                }
                val thumbnail = try {
                    Uri.parse(getString(thumbnailIndex))
                } catch (e: Exception) {
                    null
                }
                contact = ContactData(
                    id = id,
                    name = displayName,
                    image = image,
                    thumbnail = thumbnail
                )
                contacts.put(id, contact)
            }
            val mimeType = getString(mimeTypeIndex)
            val emails: MutableList<String> = mutableListOf()
            val phoneNumbers: MutableList<String> = mutableListOf()
            when (mimeType) {
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE ->
                    getString(data1Index)?.let { emails.add(it) }

                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE ->
                    getString(data1Index)?.let { phoneNumbers.add(it) }

            }
            contact.email = emails.firstOrNull { Patterns.EMAIL_ADDRESS.matcher(it).matches() }.orEmpty()
            contact.phone = phoneNumbers.firstOrNull { it.isNotEmpty() }.orEmpty()

            moveToNext()
        }

        return contacts.valueIterator()
            .asSequence()
            .filter { it.id >= 0 && it.name.isNotEmpty() }
            .toList()
    }
}

data class ContactData(
    val id: Long,
    val name: String,
    var email: String = "",
    var phone: String = "",
    val thumbnail: Uri?,
    val image: Uri?
)
