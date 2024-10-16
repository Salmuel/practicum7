package iu.c323.fall2024.practicum7

import android.content.Context
import androidx.room.Room
import iu.c323.fall2024.practicum7.database.TicketDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class TicketRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {
    private val database: TicketDatabase = Room.databaseBuilder(
        context.applicationContext,
        TicketDatabase::class.java,
        DATABASE_NAME
    )
        .build()

    fun getTickets(): Flow<List<Ticket>> = database.ticketDao().getTickets()

    fun getTicket(id: UUID): Flow<Ticket> = database.ticketDao().getTicket(id)

    fun updateTicket(ticket: Ticket) {
        coroutineScope.launch {
            database.ticketDao().updateTicket(ticket)
        }
    }

    fun addTicket(ticket: Ticket) {
        coroutineScope.launch{
            database.ticketDao().addTicket(ticket)
        }
    }

    companion object {
        private var INSTANCE: TicketRepository? = null
        private const val DATABASE_NAME = "ticket-database"

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TicketRepository(context)
            }
        }

        fun get(): TicketRepository {
            return INSTANCE ?: throw IllegalStateException("TicketRepository must be initialized")
        }
    }
}