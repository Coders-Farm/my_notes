package com.example.mynotes.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.mynotes.models.NotesResponse
import com.example.mynotes.repository.NotesRepository
import com.example.mynotes.utils.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
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
import java.util.Date

class NotesListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var notesRepository: NotesRepository

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
    fun getAllNotes_expected_success() = runTest{
        val notes = flowOf(listOf(NotesResponse(1, "Test Note", "This is a test note", createdAt = Date())))
        Mockito.`when`(notesRepository.getAllNotes()).thenReturn(notes.stateIn(this))

        val sut = NotesListViewModel(notesRepository)
        sut.getAllNotes()
        testDispatcher.scheduler.advanceUntilIdle()
        sut.stateFlow.test {
            val result = awaitItem()
            Assert.assertEquals(true, result?.data?.get(0)?.id == 1)
            Assert.assertEquals(true, result is NetworkResponse.Success)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getAllNotes_expected_error() = runTest{
        Mockito.`when`(notesRepository.getAllNotes()).thenReturn(null)

        val sut = NotesListViewModel(notesRepository)
        sut.getAllNotes()
        testDispatcher.scheduler.advanceUntilIdle()
        sut.stateFlow.test {
            val result = awaitItem()
            Assert.assertEquals(false, result?.message == null)
            Assert.assertEquals(true, result is NetworkResponse.Error)

            cancelAndIgnoreRemainingEvents()
        }
    }
}