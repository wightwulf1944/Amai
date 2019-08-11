package i.am.shiro.amai.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.DownloadJobAdapter;
import i.am.shiro.amai.model.DownloadJob;
import io.realm.Realm;
import io.realm.RealmResults;

public class DownloadsFragment extends Fragment {

    private Realm realm;

    private RealmResults<DownloadJob> downloadJobs;

    public DownloadsFragment() {
        super(R.layout.fragment_downloads);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        downloadJobs = realm.where(DownloadJob.class)
                .findAll();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DownloadJobAdapter adapter = new DownloadJobAdapter();
        adapter.submitList(downloadJobs);
        downloadJobs.addChangeListener(list -> adapter.submitList(list));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
    }
}
