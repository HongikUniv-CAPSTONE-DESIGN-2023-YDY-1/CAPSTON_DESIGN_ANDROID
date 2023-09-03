package com.example.capstonedesign.data


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreferences(
    context: Context
) {
    private val applicationContext = context.applicationContext
    private val dataStore : DataStore<Preferences>
    init {
        dataStore = applicationContext.createDataStore(
            name = "my_data_store"
        )
    }

    val accessToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }
    val refreshToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN]
        }

    val userEmail: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[USER_EMAIL]
        }
    val userPassword: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[USER_PASSWORD]
        }
    suspend fun saveToken(accessToken: String, refreshToken: String){
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }
    suspend fun saveEmailAndPassword(email: String, password: String){
        dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
            preferences[USER_PASSWORD] = password
        }
    }

    companion object{
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_PASSWORD = stringPreferencesKey("user_password")
    }

}