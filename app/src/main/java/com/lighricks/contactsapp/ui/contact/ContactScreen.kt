package com.lighricks.contactsapp.ui.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lighricks.contactsapp.R
import com.lighricks.contactsapp.ui.theme.ContactDataBackground
import com.lighricks.contactsapp.ui.theme.UserdataText
import com.lighricks.contactsapp.ui.theme.UsernameText
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun ContactScreen(userId: Long) {
    val systemUiController: SystemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = false

    ContactScreenUi()
}

@Preview
@Composable
private fun ContactScreenUi() {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val bottomMargin: Dp = this.maxHeight / 10f
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = R.drawable.ic_contact_placeholder, // replace with contact.image
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
                text = "Andi Lightricksovitch",
                isTitle = true
            )
            UserDataText(
                text = "android.devices@lightricks.com",
                isTitle = false,
                fontStyle = FontStyle.Italic
            )
            UserDataText(
                text = "+972 525 111 111",
                isTitle = false
            )
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
