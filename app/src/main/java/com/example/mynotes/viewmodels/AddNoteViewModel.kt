package com.example.mynotes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.models.NotesResponse
import com.example.mynotes.repository.NotesRepository
import com.example.mynotes.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(private val notesRepository: NotesRepository):ViewModel() {

    private val createNoteMutableStateFlow = MutableSharedFlow<NetworkResponse<String>>()

    val createNoteStateFlow:SharedFlow<NetworkResponse<String>>
        get() = createNoteMutableStateFlow

    fun createNote(notesResponse: NotesResponse){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                createNoteMutableStateFlow.emit(NetworkResponse.Loading())
                val result = notesRepository.createNote(notesResponse)
                createNoteMutableStateFlow.emit(NetworkResponse.Success(result.data!!))
            }catch (e:Exception){
                createNoteMutableStateFlow.emit(NetworkResponse.Error(e.message.toString()))
            }
        }
    }

    private val updateNoteMutableStateFlow = MutableSharedFlow<NetworkResponse<String>>()

    val updateNoteStateFlow:MutableSharedFlow<NetworkResponse<String>>
        get() = updateNoteMutableStateFlow

    fun updateNote(notesResponse: NotesResponse){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateNoteMutableStateFlow.emit(NetworkResponse.Loading())
                val result = notesRepository.updateNote(notesResponse)
                updateNoteMutableStateFlow.emit(NetworkResponse.Success(result.data!!))
            }catch (e:Exception){
                updateNoteMutableStateFlow.emit(NetworkResponse.Error(e.message.toString()))
            }
        }
    }

    private val deleteNoteMutableStateFlow = MutableSharedFlow<NetworkResponse<String>>()

    val deleteNoteStateFlow:MutableSharedFlow<NetworkResponse<String>>
        get() = deleteNoteMutableStateFlow

    fun deleteNote(notesResponse: NotesResponse){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteNoteMutableStateFlow.emit(NetworkResponse.Loading())
                val result = notesRepository.deleteNote(notesResponse)
                deleteNoteMutableStateFlow.emit(NetworkResponse.Success(result.data!!))
            }catch (e:Exception){
                deleteNoteMutableStateFlow.emit(NetworkResponse.Error(e.message.toString()))
            }
        }
    }
}