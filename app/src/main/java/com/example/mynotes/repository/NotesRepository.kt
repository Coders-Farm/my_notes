package com.example.mynotes.repository

import com.example.mynotes.db.MyNotesDB
import com.example.mynotes.models.NotesResponse
import com.example.mynotes.utils.Constants
import com.example.mynotes.utils.NetworkResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class NotesRepository @Inject constructor(private val myNotesDB: MyNotesDB) {

    suspend fun createNote(notesResponse: NotesResponse):NetworkResponse<String>{
        myNotesDB.getNotesDao().createNote(notesResponse)
        return NetworkResponse.Success(Constants.SuccessMessages.NOTE_CREATED_SUCCESSFULLY)
    }

    suspend fun updateNote(notesResponse: NotesResponse):NetworkResponse<String>{
        myNotesDB.getNotesDao().updateNote(notesResponse)
        return NetworkResponse.Success(Constants.SuccessMessages.NOTE_UPDATED_SUCCESSFULLY)
    }

    suspend fun deleteNote(notesResponse: NotesResponse):NetworkResponse<String>{
        myNotesDB.getNotesDao().deleteNote(notesResponse)
        return NetworkResponse.Success(Constants.SuccessMessages.NOTE_DELETED_SUCCESSFULLY)
    }

    suspend fun getAllNotes():StateFlow<List<NotesResponse>>{
        val result = myNotesDB.getNotesDao().getAllNotes()
            .stateIn(CoroutineScope(Dispatchers.IO))
        return result
    }
}