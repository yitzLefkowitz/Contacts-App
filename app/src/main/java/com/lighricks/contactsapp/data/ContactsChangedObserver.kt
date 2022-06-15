package com.lighricks.contactsapp.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.os.Handler
import android.os.HandlerThread
import android.provider.ContactsContract
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.lighricks.contactsapp.coroutine.AppScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private const val CONTACTS_PROCESSING_THREAD = "Contacts Processing Thread"

@Singleton
class ContactsChangedObserver @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contactsCache: ContactsCache,
    private val appScope: AppScope
) : ContentObserver(
    Handler(HandlerThread(CONTACTS_PROCESSING_THREAD).apply { this.start() }.looper)
) {

    @RequiresPermission(Manifest.permission.READ_CONTACTS)
    fun observe() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            throw IllegalStateException("Need to get permission to read contacts first")
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
            contactsCache.updateContacts(contacts)
        }
    }
}
