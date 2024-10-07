package com.example.mynotes.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mynotes.dao.NotesDao
import com.example.mynotes.db.converters.DateToLongConverter
import com.example.mynotes.models.NotesResponse

@TypeConverters(value = [DateToLongConverter::class])
@Database(entities = [NotesResponse::class], version = 1)
abstract class MyNotesDB:RoomDatabase() {

    abstract fun getNotesDao():NotesDao

}