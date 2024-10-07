package com.example.mynotes.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mynotes.models.UserRequest
import com.example.mynotes.models.UserResponse
import com.example.mynotes.service.RegistrationService
import com.example.mynotes.utils.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.internal.matchers.Null
import org.mockito.kotlin.mock
import retrofit2.Response

class RegistrationRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var registrationService:RegistrationService

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun registration_create_acc_success() = runTest{
        val userRequest = UserRequest("eve.holt@reqres.in","pistol")
        `when`(registrationService.createAccount(userRequest)).thenReturn(Response.success(UserResponse(id = 4,token = "QpwL5tke4Pnpja7X4")))

        val sut = RegistrationRepository(registrationService)
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.createAccount(userRequest)
        assertEquals(4,result.data?.id)
        assertEquals("QpwL5tke4Pnpja7X4",result.data?.token)
    }

    @Test
    fun registration_create_acc_error() = runTest{
        val userRequest = UserRequest("abc@gmail.com","123456")
        `when`(registrationService.createAccount(userRequest)).thenReturn(Response.error(404,"Unauthorized".toResponseBody()))

        val sut = RegistrationRepository(registrationService)
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.createAccount(userRequest)
        assertEquals("Error fetching Data.",result.message)
    }

    @Test
    fun registration_login_success() = runTest{
        val userRequest = UserRequest("eve.holt@reqres.in","cityslicka")
        `when`(registrationService.login(userRequest)).thenReturn(Response.success(UserResponse(id = null,token = "QpwL5tke4Pnpja7X4")))

        val sut = RegistrationRepository(registrationService)
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.login(userRequest)
        assertEquals(false,result.data?.token == null)
    }

    @Test
    fun registration_login_error() = runTest{
        val userRequest = UserRequest("eve.holt@reqres.in","cityslicka")
        `when`(registrationService.login(userRequest)).thenReturn(Response.error(404,"Unauthorized".toResponseBody()))

        val sut = RegistrationRepository(registrationService)
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.login(userRequest)
        assertEquals("Error fetching Data.",result.message)
    }

}