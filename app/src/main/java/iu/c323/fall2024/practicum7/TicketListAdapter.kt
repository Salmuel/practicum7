package iu.c323.fall2024.practicum7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iu.c323.fall2024.practicum7.databinding.ListItemTicketBinding
import android.widget.Toast
import java.util.UUID

/**changes**/
class TicketHolder( private val binding: ListItemTicketBinding)
    : RecyclerView.ViewHolder(binding.root) {
    fun bind(ticket: Ticket, onTicketClicked:(ticketId: UUID) -> Unit) {
        binding.ticketTitle.text = ticket.title
        binding.ticketDate.text = ticket.date.toString()

        binding.root.setOnClickListener {
            onTicketClicked(ticket.id)
        }

        binding.ticketSolved.visibility = if (ticket.isSolved)  {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}

class TicketListAdapter(
    private val tickets: List<Ticket>,
    private val onTicketClicked: (ticketId: UUID) -> Unit
) : RecyclerView.Adapter<TicketHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = ListItemTicketBinding.inflate(inflator, parent, false)
        return TicketHolder(binding)
    }

    override fun getItemCount(): Int {
        return tickets.size
    }

    override fun onBindViewHolder(holder: TicketHolder, position: Int) {
        val ticket = tickets[position]
        holder.bind(ticket,onTicketClicked)
        }
    }