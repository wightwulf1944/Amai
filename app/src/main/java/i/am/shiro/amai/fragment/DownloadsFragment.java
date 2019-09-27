package i.am.shiro.amai.fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.DownloadJobAdapter;
import i.am.shiro.amai.fragment.dialog.PlaceholderDialogFragment;
import i.am.shiro.amai.viewmodel.DownloadsFragmentModel;

public class DownloadsFragment extends Fragment {

    public DownloadsFragment() {
        super(R.layout.fragment_downloads);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(this::onActionClick);

        DownloadJobAdapter adapter = new DownloadJobAdapter();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);

        ViewModelProviders.of(this)
            .get(DownloadsFragmentModel.class)
            .observeDownloadJobs(this, adapter::submitList);
    }

    private boolean onActionClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_resume_all:
                showPlaceholder();
                break;
            case R.id.action_pause_all:
                showPlaceholder();
                break;
        }
        return true;
    }

    private void showPlaceholder() {
        PlaceholderDialogFragment dialogFragment = new PlaceholderDialogFragment();
        dialogFragment.show(getChildFragmentManager(), null);
    }
}
