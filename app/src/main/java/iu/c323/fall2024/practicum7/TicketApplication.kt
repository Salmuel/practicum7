package iu.c323.fall2024.practicum7

import android.app.Application
import iu.c323.fall2024.practicum7.database.TicketRepository

class TicketApplication : Application () {
    override fun onCreate() {
        super.onCreate()
        TicketRepository.initialize(this)
    }
}