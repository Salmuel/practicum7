package iu.c323.fall2024.practicum7.database

import iu.c323.fall2024.practicum7.database.TicketDao
import android.content.Context
import androidx.room.Room
import iu.c323.fall2024.practicum7.Ticket
import kotlinx.coroutines.flow.Flow
import java.util.UUID

private const val DATABASE_NAME = "ticket-database"

class TicketRepository private constructor(context: Context) {
    private val database: TicketDatabase = Room.databaseBuilder(
        context.applicationContext,
        TicketDatabase::class.java,
        DATABASE_NAME
    )
        .addMigrations(migration_1_2)
        .build()

    private val ticketDao: TicketDao = database.ticketDao()

    fun getTickets(): Flow<List<Ticket>> = ticketDao.getTickets()
    suspend fun getTicket(id: UUID): Flow<Ticket> = ticketDao.getTicket(id)

    companion object {
        private var INSTANCE: TicketRepository? = null

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