package i.am.shiro.amai.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import i.am.shiro.amai.Preferences;
import i.am.shiro.amai.R;
import i.am.shiro.amai.model.StorageOption;
import i.am.shiro.amai.util.StorageUtil;

import java.util.List;

public class StorageIntroFragment extends Fragment {

    private List<StorageOption> storageOptions;

    private int selectedIndex;

    @Override
    public void onAttach(Context context) {
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
            selectedIndex = savedInstanceState.getInt("selectedIndex", -1);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedIndex", selectedIndex);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_storage, container, false);

        LinearLayout optionsLayout = view.findViewById(R.id.optionsLayout);

        for (int i = 0; i < storageOptions.size(); i++) {
            StorageOption storageOption = storageOptions.get(i);
            int index = i;

            View storageOptionView = inflater.inflate(R.layout.item_storage_option, optionsLayout, false);
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

        return view;
    }
}
