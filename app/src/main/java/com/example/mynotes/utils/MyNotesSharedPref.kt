package com.example.mynotes.utils

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MyNotesSharedPref @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(Constants.SharedPrefKeys.MY_NOTES_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(Constants.SharedPrefKeys.MY_NOTES_TOKEN, null)
    }

}