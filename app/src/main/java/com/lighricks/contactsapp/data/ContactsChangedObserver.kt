package com.lighricks.contactsapp.data

import android.Manifest
import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.HandlerThread
import android.provider.ContactsContract
import android.util.Log
import androidx.annotation.RequiresPermission
import com.lighricks.contactsapp.coroutine.AppScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private const val CONTACTS_PROCESSING_THREAD = "Contacts Processing Thread"

@Singleton
class ContactsChangedObserver @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contactsStorage: ContactsStorage,
    private val appScope: AppScope
) : ContentObserver(
    Handler(HandlerThread(CONTACTS_PROCESSING_THREAD).apply { this.start() }.looper)
) {

    @RequiresPermission(Manifest.permission.READ_CONTACTS)
    fun observe() {
        try {
            context.contentResolver.unregisterContentObserver(this)
        } catch (e: Exception) {
            // do nothing
            Log.d("ContactsChangedObserver", "Error unregistering contacts observer", e)
        }
        context.contentResolver.registerContentObserver(
            ContactsContract.Data.CONTENT_URI,
            true,
            this
        )
    }

    override fun onChange(selfChange: Boolean) {
        appScope.launch {
            val contacts = ContactsRetriever.getDeviceContacts(context)
            contactsStorage.updateContacts(contacts)
        }
    }
}
