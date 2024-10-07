package com.example.mynotes.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.mynotes.models.UserRequest
import com.example.mynotes.models.UserResponse
import com.example.mynotes.repository.RegistrationRepository
import com.example.mynotes.utils.MyNotesSharedPref
import com.example.mynotes.utils.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.never
import org.mockito.kotlin.verify

class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var registrationRepository: RegistrationRepository

    @Mock
    private lateinit var myNotesSharedPref: MyNotesSharedPref

    private val testDispatcher = StandardTestDispatcher()

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
    fun login_expected_success() = runTest{
        val userRequest = UserRequest("test@gmail.com","123456")
        Mockito.`when`(registrationRepository.login(userRequest)).thenReturn(NetworkResponse.Success(
            UserResponse(id = null, token = "abcdfhgtihjy")
        ))

        val sut = LoginViewModel(registrationRepository,myNotesSharedPref)
        sut.login(userRequest)
        testDispatcher.scheduler.advanceUntilIdle()
        sut.stateFlow.test{
            val result = awaitItem()
            assertEquals(false,result?.data?.token == null)
            assertEquals(true,result is NetworkResponse.Success)

            verify(myNotesSharedPref).saveToken("abcdfhgtihjy")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun login_expected_error() = runTest{
        val userRequest = UserRequest("test@gmail.com","123456")
        Mockito.`when`(registrationRepository.login(userRequest)).thenReturn(NetworkResponse.Error("Something went wrong"))

        val sut = LoginViewModel(registrationRepository,myNotesSharedPref)
        sut.login(userRequest)
        testDispatcher.scheduler.advanceUntilIdle()
        sut.stateFlow.test{
            val result = awaitItem()
            assertEquals("Something went wrong",result?.message)
            assertEquals(true,result is NetworkResponse.Error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun login_expected_loading() = runTest{
        val userRequest = UserRequest("test@gmail.com","123456")
        Mockito.`when`(registrationRepository.login(userRequest)).thenReturn(NetworkResponse.Loading())

        val sut = LoginViewModel(registrationRepository,myNotesSharedPref)
        sut.login(userRequest)
        testDispatcher.scheduler.advanceUntilIdle()
        sut.stateFlow.test{
            val result = awaitItem()
            assertEquals(true,result is NetworkResponse.Loading)

            cancelAndIgnoreRemainingEvents()
        }
    }
}