package i.am.shiro.amai.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import i.am.shiro.amai.Preferences;
import i.am.shiro.amai.R;
import i.am.shiro.amai.model.StorageOption;
import i.am.shiro.amai.util.StorageUtil;

public final class StorageSetupFragment extends Fragment {

    private static final String KEY_SELECTED = "selectedIndex";

    private List<StorageOption> storageOptions;

    private int selectedIndex;

    public StorageSetupFragment() {
        super(R.layout.fragment_storage_setup);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        storageOptions = StorageUtil.getStorageOptions(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            selectedIndex = 0;
            Preferences.setStoragePath(storageOptions.get(0).getPath());
        } else {
            selectedIndex = savedInstanceState.getInt(KEY_SELECTED, -1);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SELECTED, selectedIndex);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.finishButton)
                .setOnClickListener(v -> onFinish());

        LinearLayout optionsLayout = view.findViewById(R.id.optionsLayout);

        for (int i = 0; i < storageOptions.size(); i++) {
            StorageOption storageOption = storageOptions.get(i);
            int index = i;

            View storageOptionView = getLayoutInflater()
                    .inflate(R.layout.item_storage_option, optionsLayout, false);
            storageOptionView.setSelected(selectedIndex == i);
            storageOptionView.setOnClickListener(v -> {
                optionsLayout.dispatchSetSelected(false);
                storageOptionView.setSelected(true);
                selectedIndex = index;
                Preferences.setStoragePath(storageOption.getPath());
            });

            TextView titleText = storageOptionView.findViewById(R.id.titleText);
            titleText.setText(storageOption.getTitle());

            TextView pathText = storageOptionView.findViewById(R.id.pathText);
            pathText.setText(storageOption.getPath());

            TextView freeSpaceText = storageOptionView.findViewById(R.id.freeSpaceText);
            freeSpaceText.setText(storageOption.getFreeSpaceStr());

            ProgressBar gauge = storageOptionView.findViewById(R.id.gauge);
            gauge.setMax(storageOption.getTotalSpaceMb());
            gauge.setProgress(storageOption.getUsedSpaceMb());

            optionsLayout.addView(storageOptionView);
        }
    }

    private void onFinish() {
        FragmentManager fragmentManager = requireFragmentManager();
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, new MainFragment())
                .commit();

        Preferences.setFirstRunDone();
    }
}