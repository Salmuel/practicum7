package iu.c323.fall2024.practicum7

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import iu.c323.fall2024.practicum7.databinding.FragmentTicketDetailBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "TicketDetailFragment"

class TicketDetailFragment : Fragment() {
    private val args: TicketDetailFragmentArgs by navArgs()
    private var _binding: FragmentTicketDetailBinding? = null
    private val binding get() = checkNotNull(_binding) {
        "Cannot access the view because it is null"
    }
    private val ticketDetailViewModel: TicketDetailViewModel by viewModels {
        TicketDetailViewModelFactory(args.ticketId)
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
                ticketDetailViewModel.updateTicket { oldTicket ->
                    oldTicket.copy(title = text.toString())
                }
            }

            ticketDate.isEnabled = false

            ticketSolved.setOnCheckedChangeListener { _, isChecked ->
                ticketDetailViewModel.updateTicket { oldTicket ->
                    oldTicket.copy(isSolved = isChecked)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                ticketDetailViewModel.ticket.collect { ticket ->
                    ticket?.let { updateUi(it) }
                }
            }
        }

        setFragmentResultListener(DatePickerFragment.REQUEST_KEY_DATE) { _, bundle ->
            val newDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as? Date
            newDate?.let { date ->
                ticketDetailViewModel.updateTicket { it.copy(date = formatDate(date)) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(ticket: Ticket) {
        binding.apply {
            if (ticketTitle.text.toString() != ticket.title) {
                ticketTitle.setText(ticket.title)
            }
            ticketDate.text = ticket.date
            ticketDate.setOnClickListener {
                findNavController().navigate(
                    TicketDetailFragmentDirections.selectDate(
                        parseDate(ticket.date) ?: Date()
                    )
                )
            }
            ticketSolved.isChecked = ticket.isSolved
        }
    }

    private fun formatDate(date: Date): String {
        return SimpleDateFormat("MMMM d, yyyy", Locale.US).format(date)
    }

    private fun parseDate(dateString: String): Date? {
        return try {
            SimpleDateFormat("MMMM d, yyyy", Locale.US).parse(dateString)
        } catch (e: Exception) {
            null
        }
    }
}