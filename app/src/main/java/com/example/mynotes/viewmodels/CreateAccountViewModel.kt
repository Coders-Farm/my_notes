package com.example.mynotes.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.example.mynotes.models.UserRequest
import com.example.mynotes.models.UserResponse
import com.example.mynotes.repository.RegistrationRepository
import com.example.mynotes.utils.Constants
import com.example.mynotes.utils.Extensions
import com.example.mynotes.utils.MyNotesSharedPref
import com.example.mynotes.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(private val registrationRepository: RegistrationRepository,private val myNotesSharedPref: MyNotesSharedPref):ViewModel() {

    private var mutableStateFlow = MutableStateFlow<NetworkResponse<UserResponse>?>(null)

    val stateFlow:StateFlow<NetworkResponse<UserResponse>?>
    get() = mutableStateFlow

    fun createAccount(userRequest: UserRequest) {
        viewModelScope.launch(Dispatchers.IO) {
                mutableStateFlow.emit(NetworkResponse.Loading())
                val response = registrationRepository.createAccount(userRequest)
                mutableStateFlow.emit(response)

                //save token
                myNotesSharedPref.saveToken(response.data?.token ?: "")
        }
    }

}
