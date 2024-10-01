package iu.c323.fall2024.practicum7.database

import androidx.room.TypeConverter
import java.util.Date


class TicketTypeConverter {

    @TypeConverter
    fun fromDate(date: Date) : Long {
        return date.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long) : Date {
        return Date(millisSinceEpoch)
    }
}