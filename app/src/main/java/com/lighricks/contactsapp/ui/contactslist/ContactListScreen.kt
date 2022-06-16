package com.lighricks.contactsapp.ui.contactslist

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lighricks.contactsapp.R
import com.lighricks.contactsapp.ui.destinations.ContactScreenDestination
import com.lighricks.contactsapp.ui.theme.AppBarBackground
import com.lighricks.contactsapp.ui.theme.ContactBackgroundGradientEnd
import com.lighricks.contactsapp.ui.theme.ContactBackgroundGradientStart
import com.lighricks.contactsapp.ui.theme.ContactDivider
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun ContactListScreen(
    viewModel: ContactsListViewModel = hiltViewModel<ContactsListViewModel>(),
    navigator: DestinationsNavigator
) {
    val systemUiController: SystemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = true

    val initialPermissionStatus = LocalContext.current.hasContactsPermission
    var hasPermission by remember { mutableStateOf(initialPermissionStatus) }
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            hasPermission = it
        }

    if (!hasPermission) {
        SideEffect {
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    } else {
        @Suppress("MissingPermission")
        ContactsScreenUi(viewModel = viewModel, navigator = navigator)
    }
}

private val Context.hasContactsPermission: Boolean
    get() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_CONTACTS
    ) == PackageManager.PERMISSION_GRANTED

@RequiresPermission(Manifest.permission.READ_CONTACTS)
@Composable
private fun ContactsScreenUi(
    viewModel: ContactsListViewModel,
    navigator: DestinationsNavigator
) {
    viewModel.observeContacts()
    val contacts: List<ContactRow> by viewModel.getContacts()
        .collectAsState(initial = emptyList())
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { AppBarTitle() },
                backgroundColor = AppBarBackground
            )
        },
        content = {
            ContactsList(
                contacts = contacts,
                onClick = { navigator.navigate(ContactScreenDestination(it.id)) }
            )
        }
    )
}

@Composable
private fun AppBarTitle() {
    Text(
        text = stringResource(id = R.string.app_bar_title),
        color = Color.White
    )
}

@Composable
private fun ContactsList(
    contacts: List<ContactRow>,
    onClick: (contact: ContactRow) -> Unit
) {
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
private fun ContactRow(contact: ContactRow, onClick: (contact: ContactRow) -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Text(
        text = contact.name,
        fontSize = 30.sp,
        color = Color.White,
        modifier = Modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        ContactBackgroundGradientStart,
                        ContactBackgroundGradientEnd
                    ),
                    start = Offset.Zero,
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ),
                shape = shape
            )
            .clip(shape)
            .fillMaxWidth()
            .clickable { onClick(contact) }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Preview
@Composable
private fun UiPreview() {
    ContactsList(contacts = (0..10)
        .map {
            ContactRow(it.toLong(), "Demo contact $it")
        },
        onClick = { }
    )
}



