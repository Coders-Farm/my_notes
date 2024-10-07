package com.example.mynotes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.models.NotesResponse
import com.example.mynotes.repository.NotesRepository
import com.example.mynotes.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(private val notesRepository: NotesRepository):ViewModel() {

    private var mutableStateFlow = MutableStateFlow<NetworkResponse<List<NotesResponse>>?>(null)

    val stateFlow:MutableStateFlow<NetworkResponse<List<NotesResponse>>?>
        get() = mutableStateFlow

    fun getAllNotes(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mutableStateFlow.emit(NetworkResponse.Loading())
                mutableStateFlow.emit(NetworkResponse.Success(notesRepository.getAllNotes().value))
            }catch (e:Exception){
                mutableStateFlow.emit(NetworkResponse.Error(e.message.toString()))
            }
        }
    }

}