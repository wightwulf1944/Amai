package i.am.shiro.amai.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import i.am.shiro.amai.R
import i.am.shiro.amai.FavoritesSort
import i.am.shiro.amai.fragment.FavoritesFragment

class FavoritesSortDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Sort by")
            .setItems(R.array.sort_items_favorites) { _, which -> onItemClick(which) }
            .create()
    }

    private fun onItemClick(which: Int) {
        val parent = parentFragment as FavoritesFragment
        parent.onSort(when (which) {
            0 -> FavoritesSort.New
            1 -> FavoritesSort.Old
            else -> throw RuntimeException()
        })
        dismiss()
    }
}
