package i.am.shiro.amai.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import i.am.shiro.amai.AmaiPreferences
import org.koin.android.ext.android.inject

class SearchConstantsDialog : DialogFragment() {

    private val preferences by inject<AmaiPreferences>()

    private lateinit var editText: AppCompatEditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        editText = AppCompatEditText(requireContext())
        editText.setText(preferences.searchConstants)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Search Constants")
            .setView(editText)
            .setPositiveButton("Save") { _, _ -> onSaveClick() }
            .setNegativeButton("Cancel") { _, _ -> dismiss() }
            .create()
    }

    private fun onSaveClick() {
        preferences.searchConstants = editText.text!!.toString()
    }
}
