package i.am.shiro.amai.fragment.dialog

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import i.am.shiro.amai.R
import i.am.shiro.amai.model.Book
import i.am.shiro.amai.util.argument
import i.am.shiro.amai.viewmodel.SavedViewModel

class DeleteBookDialogFragment() : DialogFragment() {

    private var bookId by argument<Int>()

    private var bookTitle by argument<String>()

    constructor(book: Book) : this() {
        bookId = book.id
        bookTitle = book.title
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_title_delete)
            .setMessage(bookTitle)
            .setPositiveButton(R.string.delete) { _, _ -> onConfirmClick() }
            .setNegativeButton(R.string.cancel) { _, _ -> dismiss() }
            .create()

    private fun onConfirmClick() {
        ViewModelProviders.of(requireParentFragment())
            .get(SavedViewModel::class.java)
            .onBookDelete(bookId)
    }
}