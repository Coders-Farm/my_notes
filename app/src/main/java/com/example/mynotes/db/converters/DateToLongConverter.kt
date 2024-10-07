package com.example.mynotes.db.converters

import androidx.room.TypeConverter
import java.util.Date


class DateToLongConverter {

    @TypeConverter
    fun fromDateToLong(date: Date):Long{
        return date.time
    }

    @TypeConverter
    fun fromLongToDate(date:Long):Date{
        return Date(date)
    }

}