package i.am.shiro.amai.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import i.am.shiro.amai.R;

/**
 * Created by Shiro on 1/15/2018.
 */

public class StorageIntroFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_storage, container, false);


        if (hasExternal()) {
            View externalView = inflater.inflate(R.layout.item_storage_option, container);
        }

        return view;
    }

    private boolean hasExternal() {
        return false;
    }
}
