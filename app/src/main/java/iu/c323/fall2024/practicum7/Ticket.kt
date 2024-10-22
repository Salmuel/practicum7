package iu.c323.fall2024.practicum7

import androidx.room.PrimaryKey
import java.util.UUID



data class Ticket(
    @PrimaryKey val id: UUID,
    val title: String,
    val date: String,
    val isSolved: Boolean,
    val assignee: String = ""

) {
    fun collect(function: () -> Unit) {

    }
}
