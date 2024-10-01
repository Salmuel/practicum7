package iu.c323.fall2024.practicum7

import androidx.lifecycle.ViewModel
import java.util.UUID
import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.viewModelScope
import iu.c323.fall2024.practicum7.database.TicketRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

private const val TAG = "TicketListViewModel"

class TicketListViewModel : ViewModel() {

    private val ticketRepository = TicketRepository.get()

    private val _tickets: MutableStateFlow<List<Ticket>> = MutableStateFlow(emptyList())
    val tickets: StateFlow<List<Ticket>>
        get() = _tickets.asStateFlow()

    init {
        viewModelScope.launch {
            ticketRepository.getTickets().collect {
                _tickets.value = it
            }
        }
    }
}