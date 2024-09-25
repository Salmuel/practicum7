package iu.c323.fall2024.practicum7

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import iu.c323.fall2024.practicum7.databinding.FragmentTicketDetailBinding
import java.util.UUID
import android.text.format.DateFormat
import java.util.Date

class TicketDetailFragment : Fragment() {
    private var _binding: FragmentTicketDetailBinding? = null
    private val binding get() = checkNotNull(_binding) {
        "Cannot access the view because it is null"
    }
    private lateinit var ticket: Ticket

    fun formatSimpleDate(date: Date): String {
        return DateFormat.format("MMMM d, yyyy", date).toString()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ticket = Ticket(
            id = UUID.randomUUID(),
            title = "",
            date = formatSimpleDate(Date()),
            isSolved = false
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTicketDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            ticketTitle.doOnTextChanged { text, _, _, _ ->
                ticket = ticket.copy(title = text.toString())
            }

            ticketDate.apply {
                text = ticket.date.toString()
                isEnabled = false
            }

            ticketSolved.setOnCheckedChangeListener { _, isChecked ->
                ticket = ticket.copy(isSolved = isChecked)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}