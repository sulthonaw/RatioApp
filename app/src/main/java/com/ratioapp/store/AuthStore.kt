package com.ratioapp.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthStore(val context: Context) {
    companion object {
        private val Context.datastore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(
            "authstore"
        )
        private val USER_TOKEN = stringPreferencesKey("user_token")
        private val USER_ID = stringPreferencesKey("user_id")
        private val DONATION_ID = stringPreferencesKey("donation_id")
    }

    val getToken: Flow<String> = context.datastore.data.map {
        it[USER_TOKEN] ?: ""
    }

    val getDonationId: Flow<String> = context.datastore.data.map {
        it[DONATION_ID] ?: ""
    }

    val getUserId: Flow<String> = context.datastore.data.map {
        it[USER_ID] ?: ""
    }

    suspend fun setToken(token: String) {
        context.datastore.edit {
            it[USER_TOKEN] = token
        }
    }

    suspend fun setDonationId(id: String) {
        context.datastore.edit {
            it[DONATION_ID] = id
        }
    }

    suspend fun setUserId(userId: String) {
        context.datastore.edit {
            it[USER_ID] = userId
        }
    }

    suspend fun clearAuth() {
        context.datastore.edit {
            it.clear()
        }
    }
}