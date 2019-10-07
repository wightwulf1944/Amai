package i.am.shiro.amai.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

import com.google.android.material.dialog.MaterialAlertDialogBuilder

import i.am.shiro.amai.R

class NhentaiSortOrderDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
                .setTitle("Sort by")
                .setItems(R.array.sort_items_nhentai) { _, which -> onItemClick(which) }
                .create()
    }

    private fun onItemClick(which: Int) {
        dismiss()
        PlaceholderDialogFragment().show(requireFragmentManager(), null)
    }
}
