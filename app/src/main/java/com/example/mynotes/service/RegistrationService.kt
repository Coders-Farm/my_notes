package com.example.mynotes.service

import com.example.mynotes.models.UserRequest
import com.example.mynotes.models.UserResponse
import com.example.mynotes.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegistrationService {

    @POST(Constants.WebConstants.REGISTER)
    suspend fun createAccount(@Body userRequest: UserRequest):Response<UserResponse>

    @POST(Constants.WebConstants.LOGIN)
    suspend fun login(@Body userRequest: UserRequest):Response<UserResponse>

}