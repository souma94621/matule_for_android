package com.example.myapplication.data.storage

import android.content.Context
import android.util.Base64
import com.example.myapplication.domain.repository.PinRepository
import java.security.MessageDigest
import java.security.SecureRandom

/** Хранит не сам PIN, а его SHA-256 хэш с солью. */
class PinRepositoryImpl(context: Context) : PinRepository {
    private val prefs = context.getSharedPreferences("pin", Context.MODE_PRIVATE)

    override fun hasPin(): Boolean = prefs.contains(KEY_HASH)

    override fun savePin(pin: String) {
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        prefs.edit()
            .putString(KEY_SALT, Base64.encodeToString(salt, Base64.NO_WRAP))
            .putString(KEY_HASH, hash(salt, pin))
            .apply()
    }

    override fun checkPin(pin: String): Boolean {
        val salt = prefs.getString(KEY_SALT, null)?.let { Base64.decode(it, Base64.NO_WRAP) } ?: return false
        val expected = prefs.getString(KEY_HASH, null) ?: return false
        return MessageDigest.isEqual(expected.toByteArray(), hash(salt, pin).toByteArray())
    }

    override fun clear() {
        prefs.edit().clear().apply()
    }

    private fun hash(salt: ByteArray, pin: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(salt + pin.toByteArray())
        return Base64.encodeToString(digest, Base64.NO_WRAP)
    }

    private companion object {
        const val KEY_SALT = "salt"
        const val KEY_HASH = "hash"
    }
}