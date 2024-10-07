package com.example.mynotes.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mynotes.dao.NotesDao
import com.example.mynotes.db.MyNotesDB
import com.example.mynotes.models.NotesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
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
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.Date

class NotesRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var myNotesDB: MyNotesDB

    @Mock
    private lateinit var notesDao: NotesDao

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        //mock dao object
        Mockito.`when`(myNotesDB.getNotesDao()).thenReturn(notesDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun createNote()= runTest {
        val notesResponse = NotesResponse(id = 0, title = "Test Note", description = "This is test note", createdAt = Date())

        val sut = NotesRepository(myNotesDB)
        val result = sut.createNote(notesResponse)
        assertEquals("Note created Successfully.",result.data)
    }

    @Test
    fun updateNote()= runTest {
        val notesResponse = NotesResponse(id = 0, title = "Test Note", description = "This is test note", createdAt = Date())

        val sut = NotesRepository(myNotesDB)
        val result = sut.updateNote(notesResponse)
        assertEquals("Note updated Successfully.",result.data)
    }

    @Test
    fun deleteNote() = runTest{
        val notesResponse = NotesResponse(id = 0, title = "Test Note", description = "This is test note", createdAt = Date())

        val sut = NotesRepository(myNotesDB)
        val result = sut.deleteNote(notesResponse)
        assertEquals("Note deleted Successfully.",result.data)
    }

    @Test
    fun getAllNotes() = runTest {
        val notes = flowOf(listOf(NotesResponse(1, "Test Note", "This is a test note", createdAt = Date())))
        Mockito.`when`(notesDao.getAllNotes()).thenReturn(notes)
        val sut = NotesRepository(myNotesDB)
        val result = sut.getAllNotes()
        assertEquals(1,result.value.size)
        assertEquals("Test Note", result.value[0].title)
    }
}