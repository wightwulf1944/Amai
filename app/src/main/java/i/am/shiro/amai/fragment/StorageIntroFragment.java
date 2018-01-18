package i.am.shiro.amai.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import i.am.shiro.amai.R;
import i.am.shiro.amai.model.StorageOption;
import i.am.shiro.amai.util.StorageUtil;

/**
 * Created by Shiro on 1/15/2018.
 */

public class StorageIntroFragment extends Fragment {

    private List<StorageOption> storageOptions;

    private int selectedIndex;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        storageOptions = StorageUtil.getStorageOptions(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_storage, container, false);

        LinearLayout optionsLayout = view.findViewById(R.id.optionsLayout);

        for (StorageOption storageOption : storageOptions) {
            View storageOptionView = inflater.inflate(R.layout.item_storage_option, optionsLayout, false);
            storageOptionView.setOnClickListener(this::setSelectedStorageOption);

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

        View defaultSelection = optionsLayout.getChildAt(selectedIndex);
        setSelectedStorageOption(defaultSelection);

        return view;
    }

    private void setSelectedStorageOption(View view) {
        ViewGroup optionsLayout = (ViewGroup) view.getParent();
        optionsLayout.dispatchSetSelected(false);
        view.setSelected(true);
        selectedIndex = optionsLayout.indexOfChild(view);
    }
}
