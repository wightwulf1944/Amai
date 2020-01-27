package i.am.shiro.amai.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.DialogFragment

import com.google.android.material.dialog.MaterialAlertDialogBuilder

import i.am.shiro.amai.Preferences

class SearchConstantsDialog : DialogFragment() {

    private lateinit var editText: AppCompatEditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val searchConstants = Preferences.getSearchConstants()

        editText = AppCompatEditText(requireContext())
        editText.setText(searchConstants)

        return MaterialAlertDialogBuilder(requireContext())
                .setTitle("Search Constants")
                .setView(editText)
                .setPositiveButton("Save") { _, _ -> onSaveClick() }
                .setNegativeButton("Cancel") { _, _ -> dismiss() }
                .create()
    }

    private fun onSaveClick() {
        val searchConstants = editText.text!!.toString()
        Preferences.setSearchConstants(searchConstants)
    }
}
