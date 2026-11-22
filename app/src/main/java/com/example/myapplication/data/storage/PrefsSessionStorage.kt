package com.example.myapplication.data.storage

import android.content.Context
import com.example.network.common.SessionStorage

/** Хранит токен и id пользователя в SharedPreferences, поэтому вход переживает перезапуск. */
class PrefsSessionStorage(context: Context) : SessionStorage {
    private val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    override var token: String?
        get() = prefs.getString("token", null)
        set(value) = prefs.edit().putString("token", value).apply()

    override var userId: String?
        get() = prefs.getString("user_id", null)
        set(value) = prefs.edit().putString("user_id", value).apply()
}