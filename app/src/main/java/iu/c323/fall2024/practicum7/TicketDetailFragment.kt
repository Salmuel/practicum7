package iu.c323.fall2024.practicum7

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "TicketDetailFragment"
private const val DATE_FORMAT   = "EEE, MMMm dd"

class TicketDetailFragment : Fragment() {
    private val args: TicketDetailFragmentArgs by navArgs()
    private var _binding: FragmentTicketDetailBinding? = null
    private val binding get() = checkNotNull(_binding) {
        "Cannot access the view because it is null"
    }
    private val ticketDetailViewModel: TicketDetailViewModel by viewModels {
        TicketDetailViewModelFactory(args.ticketId)
    }

    private val selectAssignee = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) {uri: Uri? ->
        //Read the result
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

            ticketAssigneet.setOnClickListener{
                selectAssignee.launch(null)
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
            ticketReport.setOnClickListener{
                val reportIntent = Intent(Intent.ACTION_SEND).apply{
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getTicketReport(ticket))
                    putExtra(
                        Intent.EXTRA_SUBJECT,
                        getString(R.string.ticket_report_subject)
                    )
                }
                val chooserIntent = Intent.createChooser (
                    reportIntent,
                    getString(R.string.send_report)
                )
                startActivity(chooserIntent)
            }
            ticketAssigneet.text = ticket.assignee.ifEmpty {
                getString(R.string.ticket_assignee_text)
            }
        }
    }

    private fun getTicketReport(ticket: Ticket): String {
        val solvedString = if (ticket.isSolved) {
            getString(R.string.ticket_report_solved)
        } else {
            getString(R.string.ticket_report_unsolved)
        }

        val dateString = DateFormat.format(DATE_FORMAT, ticket.date).toString()
        val assigneeText = if (ticket.assignee.isBlank()) {
            getString(R.string.ticket_report_no_assignee)
        } else {
            getString(R.string.ticket_report_assignee, ticket.assignee)
        }

        return getString(
            R.string.ticket_report,
            ticket.title, dateString, solvedString, assigneeText
        )
    }

    private fun parseContactSelection(contactUri: Uri) {
        val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)

        val queryCursor = requireActivity().contentResolver
            .query(contactUri, queryFields, null, null, null)

        queryCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val assignee = cursor.getString(0)
                ticketDetailsViewModel.updateTicket { oldTicket ->
                    oldTicket.copy(assignee = assignee)
                }
            }
        }
    }

    private fun canResolveIntent(intent: Intent): Boolean {
        val packageManager: PackageManager = requireActivity().packageManager
        val resolvedActivity: ResolveInfo? =
            packageManager.resolveActivity(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
        return resolvedActivity != null
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