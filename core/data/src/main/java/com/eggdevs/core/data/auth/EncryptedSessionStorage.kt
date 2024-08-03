package com.eggdevs.core.data.auth

import android.content.SharedPreferences
import com.eggdevs.core.data.models.AuthInfoSerializable
import com.eggdevs.core.data.models.mappers.toAuthInfo
import com.eggdevs.core.data.models.mappers.toAuthInfoSerializable
import com.eggdevs.core.domain.SessionStorage
import com.eggdevs.core.domain.models.AuthInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EncryptedSessionStorage(
    private val sharedPreferences: SharedPreferences
): SessionStorage {
    override suspend fun setInfo(authInfo: AuthInfo?) {
        withContext(Dispatchers.IO) {
            if (authInfo == null) {
                sharedPreferences.edit().remove(KEY_AUTH_INFO).commit()
                return@withContext
            }
            val json = Json.encodeToString(authInfo.toAuthInfoSerializable())
            sharedPreferences
                .edit()
                .putString(KEY_AUTH_INFO, json)
                .commit()
        }
    }

    override suspend fun getInfo(): AuthInfo? {
        return withContext(Dispatchers.IO) {
            val authInfoJsonString = sharedPreferences.getString(KEY_AUTH_INFO, null)
            authInfoJsonString?.let {
                Json.decodeFromString<AuthInfoSerializable>(it).toAuthInfo()
            }
        }
    }

    companion object {
        const val KEY_AUTH_INFO = "KEY_AUTH_INFO"
    }
}