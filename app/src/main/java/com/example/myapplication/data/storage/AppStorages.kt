package com.example.myapplication.data.storage

import android.content.Context
import com.example.myapplication.domain.model.HomeData
import com.example.myapplication.domain.model.RegistrationDraft
import com.example.myapplication.domain.repository.HomeCache
import com.example.myapplication.domain.repository.LastScreenRepository
import com.example.myapplication.domain.repository.RegistrationDraftRepository
import com.google.gson.Gson

/** Кэш главной страницы в SharedPreferences (в виде JSON). */
class PrefsHomeCache(context: Context) : HomeCache {
    private val prefs = context.getSharedPreferences("home_cache", Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun save(data: HomeData) {
        prefs.edit().putString("data", gson.toJson(data.copy(fromCache = false))).apply()
    }

    override fun load(): HomeData? {
        val json = prefs.getString("data", null) ?: return null
        return try {
            gson.fromJson(json, HomeData::class.java)
        } catch (e: Exception) {
            null
        }
    }
}

/** Последний экран пользователя. */
class LastScreenRepositoryImpl(context: Context) : LastScreenRepository {
    private val prefs = context.getSharedPreferences("last_screen", Context.MODE_PRIVATE)

    override var route: String?
        get() = prefs.getString("route", null)
        set(value) = prefs.edit().putString("route", value).apply()

    override var tab: Int
        get() = prefs.getInt("tab", 0)
        set(value) = prefs.edit().putInt("tab", value).apply()

    override var overlay: String
        get() = prefs.getString("overlay", "").orEmpty()
        set(value) = prefs.edit().putString("overlay", value).apply()

    override fun clear() = prefs.edit().clear().apply()
}

/** Черновик регистрации: имя, отчество, фамилия, дата рождения, пол, почта. */
class RegistrationDraftRepositoryImpl(context: Context) : RegistrationDraftRepository {
    private val prefs = context.getSharedPreferences("registration_draft", Context.MODE_PRIVATE)

    override fun load() = RegistrationDraft(
        firstName = prefs.getString("first_name", "").orEmpty(),
        middleName = prefs.getString("middle_name", "").orEmpty(),
        lastName = prefs.getString("last_name", "").orEmpty(),
        birthDate = prefs.getString("birth_date", "").orEmpty(),
        gender = prefs.getString("gender", null),
        email = prefs.getString("email", "").orEmpty()
    )

    override fun save(draft: RegistrationDraft) {
        prefs.edit()
            .putString("first_name", draft.firstName)
            .putString("middle_name", draft.middleName)
            .putString("last_name", draft.lastName)
            .putString("birth_date", draft.birthDate)
            .putString("gender", draft.gender)
            .putString("email", draft.email)
            .apply()
    }

    override fun clear() = prefs.edit().clear().apply()
}