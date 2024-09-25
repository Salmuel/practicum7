package iu.c323.fall2024.practicum7

import androidx.lifecycle.ViewModel
import java.util.UUID
import android.text.format.DateFormat
import java.util.Date


class TicketListViewModel : ViewModel() {
    val tickets = mutableListOf<Ticket>()

    fun formatSimpleDate(date: Date): String {
        return DateFormat.format("MMMM d, yyyy", date).toString()
    }

    init {
        for (i in 0 until 100) {
            val ticket = Ticket(
                id = UUID.randomUUID(),
                title = "ticket #$i",
                date = formatSimpleDate(Date()),
                isSolved = i % 2 == 0
            )
            tickets += ticket
        }
    }
}