package i.am.shiro.amai.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import i.am.shiro.amai.model.DownloadJob;
import io.realm.Realm;
import io.realm.RealmResults;

public final class DownloadsFragmentModel extends ViewModel {

    private final Realm realm = Realm.getDefaultInstance();

    private final MutableLiveData<List<DownloadJob>> downloadJobs = new MutableLiveData<>();

    public DownloadsFragmentModel() {
        RealmResults<DownloadJob> realmDownloadJobs = realm.where(DownloadJob.class)
            .findAll();

        downloadJobs.setValue(realmDownloadJobs);
        realmDownloadJobs.addChangeListener(downloadJobs::setValue);
    }

    public void observeDownloadJobs(LifecycleOwner owner, Observer<List<DownloadJob>> observer) {
        downloadJobs.observe(owner, observer);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        realm.close();
    }
}
