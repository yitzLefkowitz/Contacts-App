package com.lightricks.contactsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lightricks.contactsapp.ui.NavGraphs
import com.lightricks.contactsapp.ui.theme.ContactsAppTheme
import com.lightricks.contactsapp.ui.theme.StatusBarBackground
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    ContactsAppTheme {
        val systemUiController: SystemUiController = rememberSystemUiController()
        systemUiController.setStatusBarColor(StatusBarBackground)
        DestinationsNavHost(navGraph = NavGraphs.root)
    }
}

@Preview
@Composable
fun Preview() {
    App()
}
