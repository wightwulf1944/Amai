package i.am.shiro.amai.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import i.am.shiro.amai.NhentaiSort

import i.am.shiro.amai.R
import i.am.shiro.amai.viewmodel.NhentaiViewModel

class NhentaiSortDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
                .setTitle("Sort by")
                .setItems(R.array.sort_items_nhentai) { _, which -> onItemClick(which) }
                .create()
    }

    private fun onItemClick(which: Int) {
        val sort = when(which) {
            0 -> NhentaiSort.New
            1 -> NhentaiSort.Popular
            else -> throw RuntimeException()
        }
        ViewModelProvider(parentFragment!!).get<NhentaiViewModel>().onSort(sort)
        dismiss()
    }
}
