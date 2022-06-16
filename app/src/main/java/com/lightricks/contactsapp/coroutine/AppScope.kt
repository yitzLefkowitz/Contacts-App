package com.lightricks.contactsapp.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppScope @Inject constructor() : CoroutineScope by MainScope()
