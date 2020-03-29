package i.am.shiro.amai.fragment.dialog

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import i.am.shiro.amai.R
import i.am.shiro.amai.dagger.component
import i.am.shiro.amai.util.argument
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers.io
import java.io.File

class DeleteBookDialog() : DialogFragment() {

    private val database by lazy { component.database }

    private val preferences by lazy { component.preferences }

    private var bookId by argument<Int>()

    private var bookTitle by argument<String>()

    constructor(bookId: Int, bookTitle: String) : this() {
        this.bookId = bookId
        this.bookTitle = bookTitle
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_title_delete)
            .setMessage(bookTitle)
            .setPositiveButton(R.string.delete) { _, _ -> onConfirmClick() }
            .setNegativeButton(R.string.cancel) { _, _ -> dismiss() }
            .create()

    private fun onConfirmClick() {
        File(preferences.storagePath)
            .resolve(bookId.toString())
            .deleteRecursively()

        Completable
            .concatArray(
                database.savedDao.deleteById(bookId),
                database.localImageDao.deleteById(bookId),
                database.bookDao.deleteOrphan()
            )
            .subscribeOn(io())
            .subscribe()
    }
}