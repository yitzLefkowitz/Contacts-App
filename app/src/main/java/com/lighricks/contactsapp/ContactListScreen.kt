package com.lighricks.contactsapp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighricks.contactsapp.ui.theme.AppBarBackground
import com.lighricks.contactsapp.ui.theme.ContactBackgroundGradientEnd
import com.lighricks.contactsapp.ui.theme.ContactBackgroundGradientStart
import com.lighricks.contactsapp.ui.theme.ContactDivider

@Preview(showBackground = true)
@Composable
fun ContactList() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { AppBarTitle() },
                backgroundColor = AppBarBackground
            )
        },
        content = {
            ContactsList(contacts = createContacts(), onClick = { Log.d("Yitz", "Clicked on $it") })
        }
    )
}

@Composable
fun AppBarTitle() {
    Text(
        text = stringResource(id = R.string.app_bar_title),
        color = Color.White
    )
}

@Composable
fun ContactsList(contacts: List<Contact>, onClick: (contact: Contact) -> Unit) {
    LazyColumn {
        itemsIndexed(contacts) { index, contact ->
            ContactRow(contact = contact, onClick = onClick)
            if (index < contacts.lastIndex) {
                Divider(color = ContactDivider, thickness = 1.dp)
            }
        }
    }
}

@Composable
fun ContactRow(contact: Contact, onClick: (contact: Contact) -> Unit) {
    Text(
        text = contact.name,
        fontSize = 30.sp,
        color = Color.White,
        modifier = Modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(ContactBackgroundGradientStart, ContactBackgroundGradientEnd),
                    start = Offset.Zero,
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick(contact) }
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

data class Contact(
    val id: String,
    val name: String
)

private fun createContacts(): List<Contact> = (0..10).map {
    Contact(id = it.toString(), name = "Contact $it")
}

