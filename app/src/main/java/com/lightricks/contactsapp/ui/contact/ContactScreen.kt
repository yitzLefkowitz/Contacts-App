package com.lightricks.contactsapp.ui.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lightricks.contactsapp.ui.theme.ContactDataBackground
import com.lightricks.contactsapp.ui.theme.UserdataText
import com.lightricks.contactsapp.ui.theme.UsernameText
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun ContactScreen(
    userId: Long,
    viewModel: ContactScreenViewModel = hiltViewModel()
) {
    val systemUiController: SystemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = false

    val contact by viewModel.getContactData(userId).collectAsState(
        initial = ContactUiData(
            id = userId,
            name = "",
            email = "",
            phone = "",
            image = null
        )
    )

    ContactScreenUi(contact)
}

@Composable
private fun ContactScreenUi(contact: ContactUiData) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val bottomMargin: Dp = this.maxHeight / 10f
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = contact.image ?: R.drawable.ic_contact_placeholder,
            placeholder = painterResource(id = R.drawable.ic_contact_placeholder),
            contentDescription = "users profile image",
            contentScale = ContentScale.Crop
        )
        Column(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomStart)
        ) {
            UserDataText(
                text = contact.name,
                isTitle = true
            )
            if (contact.showEmail) {
                UserDataText(
                    text = contact.email,
                    isTitle = false,
                    fontStyle = FontStyle.Italic
                )
            }
            if (contact.showPhone) {
                UserDataText(
                    text = contact.phone,
                    isTitle = false
                )
            }
            Spacer(modifier = Modifier.height(bottomMargin))
        }
    }
}

@Composable
private fun UserDataText(text: String, isTitle: Boolean, fontStyle: FontStyle? = null) {
    Text(
        text = text,
        color = if (isTitle) UsernameText else UserdataText,
        fontSize = if (isTitle) 30.sp else 24.sp,
        fontWeight = if (isTitle) FontWeight.Bold else null,
        fontStyle = fontStyle,
        modifier = Modifier
            .padding(horizontal = if (isTitle) 12.dp else 16.dp, vertical = 4.dp)
            .background(ContactDataBackground)
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .wrapContentSize()
    )
}

@Preview
@Composable
private fun UiPreview() {
    ContactScreenUi(
        ContactUiData(
            id = 0,
            name = "Andi Lightricksovitch",
            email = "android.devices@lightricks.com",
            phone = "+972 525 111 111",
            image = null
        )
    )
}

