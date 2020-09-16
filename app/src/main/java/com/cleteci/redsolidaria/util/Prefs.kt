package com.cleteci.redsolidaria.util

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    val PREFS_NAME = "com.cleteci.redsolidaria"
    val SHARED_NAME = "shared_first_time"
    val LOGIN_LATER = "shared_login_later"
    val USER_SAVED = "shared_user_saved"
    val TYPE_USER = "shared_user_type"
    val TOKEN = "token"

    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)

    var first_time: Boolean
        get() = prefs.getBoolean(SHARED_NAME, true)
        set(value) = prefs.edit().putBoolean(SHARED_NAME, value).apply()


    var login_later: Boolean
        get() = prefs.getBoolean(LOGIN_LATER, false)
        set(value) = prefs.edit().putBoolean(LOGIN_LATER, value).apply()


    var is_provider_service: Boolean
        get() = prefs.getBoolean(TYPE_USER, false)
        set(value) = prefs.edit().putBoolean(TYPE_USER, value).apply()


    var user_saved: String?
        get() = prefs.getString(USER_SAVED, null)
        set(value) = prefs.edit().putString(USER_SAVED, value).apply()

    var token: String?
        get() = prefs.getString(TOKEN, null)
        set(value) = prefs.edit().putString(TOKEN, value).apply()

    fun logout() {
        login_later = false
        user_saved = null
    }


}