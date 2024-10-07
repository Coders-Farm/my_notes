package com.example.mynotes.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mynotes.db.MyNotesDB
import com.example.mynotes.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideMyNotesDB(@ApplicationContext context: Context): MyNotesDB {
        return Room
            .databaseBuilder(context, MyNotesDB::class.java, Constants.DatabaseConstants.MY_NOTES)
            .build()
    }

}