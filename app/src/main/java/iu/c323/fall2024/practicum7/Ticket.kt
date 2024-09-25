package iu.c323.fall2024.practicum7

import java.util.UUID


data class Ticket(
    val id: UUID,
    val title: String,
    val date: String,
    val isSolved: Boolean

)
