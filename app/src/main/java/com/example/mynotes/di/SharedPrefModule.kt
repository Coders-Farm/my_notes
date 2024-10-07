package com.example.mynotes.di

import android.content.Context
import android.content.SharedPreferences
import com.example.mynotes.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SharedPrefModule {

    @Singleton
    @Provides
    fun provideSharedPref(@ApplicationContext context: Context):SharedPreferences{
        return context.getSharedPreferences(Constants.SharedPrefKeys.MY_NOTES_PREF,Context.MODE_PRIVATE)
    }

}