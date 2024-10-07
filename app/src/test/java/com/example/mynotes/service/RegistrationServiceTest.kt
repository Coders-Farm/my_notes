package com.example.mynotes.service

import Helper
import com.example.mynotes.models.UserRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistrationServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var registrationService: RegistrationService

    private var testDispatcher = StandardTestDispatcher()

    @Before
    fun before(){
        Dispatchers.setMain(testDispatcher)
        mockWebServer = MockWebServer()

        registrationService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RegistrationService::class.java)
    }

    @Test
    fun service_create_acc_expected_error() = runTest{
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(404)
        mockResponse.setBody("Something went wrong")
        mockWebServer.enqueue(mockResponse)

        val result = registrationService.createAccount(UserRequest(email = "test@gmail.com", password = "123456"))
        mockWebServer.takeRequest()

        Assert.assertEquals(false, result.isSuccessful)
        Assert.assertEquals(404, result.code())
    }

    @Test
    fun service_create_acc_expected_empty_response() = runTest{
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(200)
        mockResponse.setBody("{}")
        mockWebServer.enqueue(mockResponse)

        val result = registrationService.createAccount(UserRequest(email = "test@gmail.com", password = "123456"))
        mockWebServer.takeRequest()

        Assert.assertEquals(true, result.isSuccessful)
        Assert.assertEquals(200, result.code())
        Assert.assertEquals(null, result.body()!!.id)
    }

    @Test
    fun service_create_acc_expected_response() = runTest{
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/CreateAccountResponse.json")
        mockResponse.setBody(content)
        mockResponse.setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        val result = registrationService.createAccount(UserRequest(email = "test@gmail.com", password = "123456"))
        mockWebServer.takeRequest()

        Assert.assertEquals(true, result.isSuccessful)
        Assert.assertEquals(200, result.code())
        Assert.assertEquals(false, result.body()!!.id == null)

    }

    @Test
    fun service_login_expected_error() = runTest{
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(404)
        mockResponse.setBody("Something went wrong")
        mockWebServer.enqueue(mockResponse)

        val result = registrationService.login(UserRequest(email = "test@gmail.com", password = "123456"))
        mockWebServer.takeRequest()

        Assert.assertEquals(404,result.code())
        Assert.assertEquals(false,result.isSuccessful)
    }

    @Test
    fun service_login_expected_empty_response() = runTest{
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(200)
        mockResponse.setBody("{}")
        mockWebServer.enqueue(mockResponse)

        val result = registrationService.login(UserRequest(email = "test@gmail.com", password = "123456"))
        mockWebServer.takeRequest()

        Assert.assertEquals(200,result.code())
        Assert.assertEquals(true,result.isSuccessful)
        Assert.assertEquals(null,result.body()!!.token)
    }

    @Test
    fun service_login_expected_response() = runTest{
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(200)
        mockResponse.setBody(Helper.readFileResource("/LoginResponse.json"))
        mockWebServer.enqueue(mockResponse)

        val result = registrationService.login(UserRequest(email = "test@gmail.com", password = "123456"))
        mockWebServer.takeRequest()

        Assert.assertEquals(200,result.code())
        Assert.assertEquals(true,result.isSuccessful)
        Assert.assertEquals(false,result.body()!!.token == null)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
        mockWebServer.shutdown()

    }
}