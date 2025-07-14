package com.fathi.electricitybillapp.util

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val USER_ID = intPreferencesKey("user_id")
    val USER_ROLE = stringPreferencesKey("user_role")
}