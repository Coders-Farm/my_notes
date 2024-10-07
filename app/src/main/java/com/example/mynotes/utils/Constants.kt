package com.example.mynotes.utils

object Constants {

    object SharedPrefKeys{
        const val MY_NOTES_PREF = "myNotesPref"
        const val MY_NOTES_TOKEN = "myNotesToken"
    }

    object WebConstants{
        const val BASE_URL = "https://reqres.in/api/"
        const val REGISTER = "register"
        const val LOGIN = "login"
    }

    object DatabaseConstants{
        const val NOTES_TABLE = "notes"
        const val MY_NOTES = "myNotes"
    }

    object BundleKeys{
        const val IS_EDIT = "isEdit"
        const val NOTES_RESPONSE = "notesResponse"
    }

    object ValidationMatchers{
        const val EMAIL = "\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"
    }

    object ErrorMessages{
        const val ERROR_FETCHING_DATA = "Error fetching Data."
        const val PLEASE_CHECK_YOUR_INTERNET_CONNECTIVITY = "Please check your internet connectivity."
    }

    object SuccessMessages{
        const val NOTE_CREATED_SUCCESSFULLY  = "Note created Successfully."
        const val NOTE_UPDATED_SUCCESSFULLY = "Note updated Successfully."
        const val NOTE_DELETED_SUCCESSFULLY = "Note deleted Successfully."
    }

}