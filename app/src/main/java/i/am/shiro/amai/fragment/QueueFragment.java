package i.am.shiro.amai.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.DownloadJobAdapter;
import i.am.shiro.amai.model.DownloadJob;
import io.realm.Realm;
import io.realm.RealmResults;

public class QueueFragment extends Fragment {

    private Realm realm;

    private RealmResults<DownloadJob> downloadJobs;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_queue, container, false);

        DownloadJobAdapter adapter = new DownloadJobAdapter();
        adapter.submitList(downloadJobs);
        downloadJobs.addChangeListener(adapter::submitList);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
