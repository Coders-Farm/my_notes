package com.example.mynotes.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.mynotes.models.NotesResponse
import com.example.mynotes.repository.NotesRepository
import com.example.mynotes.utils.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.Date

class AddNoteViewModelTest{

    @get:Rule
    val instantTaskExecutorRule =InstantTaskExecutorRule()

    @Mock
    private lateinit var notesRepository: NotesRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun createNote_expected_success() = runTest{
        val notes = NotesResponse(0, "Test Note", "This is a test note", createdAt = Date())
        Mockito.`when`(notesRepository.createNote(notes)).thenReturn(NetworkResponse.Success("Note created successfully"))

        val sut = AddNoteViewModel(notesRepository)
        sut.createNote(notes)
        sut.createNoteStateFlow.test {
            val result = awaitItem()
            assertEquals("Note created successfully",result.data)
            assertEquals(true,result is NetworkResponse.Success)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun createNote_expected_error() = runTest{
        val notes = NotesResponse(0, "Test Note", "This is a test note", createdAt = Date())
        Mockito.`when`(notesRepository.createNote(notes)).thenReturn(NetworkResponse.Error("Something went wrong"))

        val sut = AddNoteViewModel(notesRepository)
        sut.createNote(notes)
        sut.createNoteStateFlow.test {
            val result = awaitItem()
            assertEquals(true,result.message != null)
            assertEquals(true,result is NetworkResponse.Error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateNote_expected_success() = runTest{
        val notes = NotesResponse(0, "Test Note", "This is a test note", createdAt = Date())
        Mockito.`when`(notesRepository.updateNote(notes)).thenReturn(NetworkResponse.Success("Note updated successfully"))

        val sut = AddNoteViewModel(notesRepository)
        sut.updateNote(notes)
        sut.updateNoteStateFlow.test {
            val result = awaitItem()
            assertEquals("Note updated successfully",result.data)
            assertEquals(true,result is NetworkResponse.Success)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateNote_expected_error() = runTest{
        val notes = NotesResponse(0, "Test Note", "This is a test note", createdAt = Date())
        Mockito.`when`(notesRepository.updateNote(notes)).thenReturn(NetworkResponse.Error("Something went wrong"))

        val sut = AddNoteViewModel(notesRepository)
        sut.updateNote(notes)
        sut.updateNoteStateFlow.test {
            val result = awaitItem()
            assertEquals(true,result.message != null)
            assertEquals(true,result is NetworkResponse.Error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteNote_expected_success() = runTest{
        val notes = NotesResponse(0, "Test Note", "This is a test note", createdAt = Date())
        Mockito.`when`(notesRepository.deleteNote(notes)).thenReturn(NetworkResponse.Success("Note deleted successfully"))

        val sut = AddNoteViewModel(notesRepository)
        sut.deleteNote(notes)
        sut.deleteNoteStateFlow.test {
            val result = awaitItem()
            assertEquals("Note deleted successfully",result.data)
            assertEquals(true,result is NetworkResponse.Success)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteNote_expected_error() = runTest{
        val notes = NotesResponse(0, "Test Note", "This is a test note", createdAt = Date())
        Mockito.`when`(notesRepository.deleteNote(notes)).thenReturn(NetworkResponse.Error("Something went wrong"))

        val sut = AddNoteViewModel(notesRepository)
        sut.deleteNote(notes)
        sut.deleteNoteStateFlow.test {
            val result = awaitItem()
            assertEquals(true,result.message != null)
            assertEquals(true,result is NetworkResponse.Error)

            cancelAndIgnoreRemainingEvents()
        }
    }
}