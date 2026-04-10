package i.am.shiro.amai.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get

import com.google.android.material.dialog.MaterialAlertDialogBuilder

import i.am.shiro.amai.R
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.network.Nhentai.Sort
import i.am.shiro.amai.viewmodel.NhentaiViewModel

@Deprecated("remove this")
class NhentaiSortDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Sort by")
            .setItems(R.array.sort_items_nhentai) { _, which -> onItemClick(which) }
            .create()
    }

    private fun onItemClick(which: Int) {
        val sort = when (which) {
            0 -> Sort.DATE
            1 -> Sort.POPULAR
            2 -> Sort.POPULAR_TODAY
            3 -> Sort.POPULAR_WEEK
            4 -> Sort.POPULAR_MONTH
            else -> throw RuntimeException()
        }
        ViewModelProvider(requireParentFragment()).get<NhentaiViewModel>().onSort(sort)
        dismiss()
    }
}
