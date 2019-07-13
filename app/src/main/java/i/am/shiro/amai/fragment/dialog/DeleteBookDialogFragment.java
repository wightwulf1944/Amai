package i.am.shiro.amai.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import i.am.shiro.amai.R;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.viewmodel.DownloadsFragmentModel;

public final class DeleteBookDialogFragment extends DialogFragment {

    private static final String KEY_BOOK_ID = "bookId";

    private static final String KEY_BOOK_TITLE = "bookTitle";

    private int bookId;

    private String bookTitle;

    public void setArguments(Book book) {
        Bundle args = new Bundle();
        args.putInt(KEY_BOOK_ID, book.getId());
        args.putString(KEY_BOOK_TITLE, book.getTitle());

        setArguments(args);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = requireArguments();
        bookId = args.getInt(KEY_BOOK_ID);
        bookTitle = args.getString(KEY_BOOK_TITLE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.dialog_title_delete)
                .setMessage(bookTitle)
                .setPositiveButton(R.string.delete, (dialog, which) -> onConfirmClick())
                .setNegativeButton(R.string.cancel, (dialog, which) -> dismiss())
                .create();
    }

    private void onConfirmClick() {
        ViewModelProviders.of(requireParentFragment())
                .get(DownloadsFragmentModel.class)
                .onBookDelete(bookId);
    }
}
