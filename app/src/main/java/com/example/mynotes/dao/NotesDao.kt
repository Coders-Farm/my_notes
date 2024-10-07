package com.example.mynotes.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mynotes.models.NotesResponse
import com.example.mynotes.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface NotesDao {

    @Insert
    suspend fun createNote(notesResponse: NotesResponse)

    @Update
    suspend fun updateNote(notesResponse: NotesResponse)

    @Delete
    suspend fun deleteNote(notesResponse: NotesResponse)

    @Query("SELECT * FROM ${Constants.DatabaseConstants.NOTES_TABLE}")
    fun getAllNotes(): Flow<List<NotesResponse>>
}