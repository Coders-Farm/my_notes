package com.example.mynotes.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mynotes.utils.Constants
import java.io.Serializable
import java.util.Date

@Entity(tableName = Constants.DatabaseConstants.NOTES_TABLE)
data class NotesResponse(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val description:String,
    val createdAt:Date,
):Serializable