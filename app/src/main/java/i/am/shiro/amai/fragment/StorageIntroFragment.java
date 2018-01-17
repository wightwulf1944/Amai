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

    private View selectedStorageOptionView;

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
            View internal = inflater.inflate(R.layout.item_storage_option, optionsLayout, false);
            internal.setTag(storageOption);
            internal.setOnClickListener(this::onStorageOptionViewClicked);

            TextView titleText = internal.findViewById(R.id.titleText);
            titleText.setText(storageOption.getTitle());

            TextView pathText = internal.findViewById(R.id.pathText);
            pathText.setText(storageOption.getPath());

            TextView freeSpaceText = internal.findViewById(R.id.freeSpaceText);
            freeSpaceText.setText(storageOption.getFreeSpaceStr());

            ProgressBar gauge = internal.findViewById(R.id.gauge);
            gauge.setMax(storageOption.getTotalSpaceMb());
            gauge.setProgress(storageOption.getUsedSpaceMb());

            optionsLayout.addView(internal);
        }

        selectedStorageOptionView = optionsLayout.getChildAt(0);
        selectedStorageOptionView.setSelected(true);

        return view;
    }

    private void onStorageOptionViewClicked(View view) {
        selectedStorageOptionView.setSelected(false);
        selectedStorageOptionView = view;
        selectedStorageOptionView.setSelected(true);

        StorageOption storageOption = (StorageOption) selectedStorageOptionView.getTag();
    }
}
