package iu.c323.fall2024.practicum7

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import java.util.Calendar

class DatePickerFragment : DialogFragment() {
    private val args:DatePickerFragmentArgs by navArgs()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        calendar.time = args.ticketDate
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDate = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            null,
            initialYear,
            initialMonth,
            initialDate
        )
    }

    companion object{
        const val REQUEST_KEY_DATE = "REQUEST_KEY_DATE"
        const val BUNDLE_KEY_DATE = "BUNDLE_KEY_DATE"

    }
}