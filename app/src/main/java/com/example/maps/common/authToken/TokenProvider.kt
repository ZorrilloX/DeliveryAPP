package com.example.maps.common.authToken

import android.content.Context
import android.content.SharedPreferences

class TokenProvider(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun clearToken() {
        prefs.edit().remove("auth_token").apply()
    }
}