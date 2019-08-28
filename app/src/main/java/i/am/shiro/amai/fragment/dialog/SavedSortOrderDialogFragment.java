package i.am.shiro.amai.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import i.am.shiro.amai.R;

public final class SavedSortOrderDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Sort by")
            .setItems(R.array.sort_items_saved, (dialog, which) -> onItemClick(which))
            .create();
    }

    private void onItemClick(int which) {
        dismiss();
        PlaceholderDialogFragment dialogFragment = new PlaceholderDialogFragment();
        dialogFragment.show(requireFragmentManager(), null);
    }
}
