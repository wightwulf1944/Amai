package i.am.shiro.amai.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import i.am.shiro.amai.Preferences;

public final class SearchConstantsDialogFragment extends DialogFragment {

    private AppCompatEditText editText;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String searchConstants = Preferences.getSearchConstants();

        editText = new AppCompatEditText(requireContext());
        editText.setText(searchConstants);

        return new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Search Constants")
            .setView(editText)
            .setPositiveButton("Save", (dialog, which) -> onSaveClick())
            .setNegativeButton("Cancel", (dialog, which) -> dismiss())
            .create();
    }

    private void onSaveClick() {
        String searchConstants = editText.getText().toString();
        Preferences.setSearchConstants(searchConstants);
    }
}
