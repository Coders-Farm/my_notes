package com.example.mynotes.repository


import com.example.mynotes.models.UserRequest
import com.example.mynotes.models.UserResponse
import com.example.mynotes.service.RegistrationService
import com.example.mynotes.utils.Constants
import com.example.mynotes.utils.NetworkResponse
import okio.IOException
import javax.inject.Inject

class RegistrationRepository @Inject constructor(private val registrationService: RegistrationService) {

    suspend fun createAccount(userRequest: UserRequest): NetworkResponse<UserResponse> {
        return try {
            val response = registrationService.createAccount(userRequest)

            if (response.isSuccessful && response.body() != null) {
                NetworkResponse.Success(response.body()!!)
            } else {
                NetworkResponse.Error(Constants.ErrorMessages.ERROR_FETCHING_DATA)
            }

        } catch (e: IOException) {
            NetworkResponse.Error(e.message.toString())
        }


    }

    suspend fun login(userRequest: UserRequest): NetworkResponse<UserResponse> {
        return try {
            val response = registrationService.login(userRequest)

            if (response.isSuccessful && response.body() != null) {
                NetworkResponse.Success(response.body()!!)
            } else {
                NetworkResponse.Error(Constants.ErrorMessages.ERROR_FETCHING_DATA)
            }

        } catch (e: IOException) {
            NetworkResponse.Error(e.message.toString())
        }

    }

}