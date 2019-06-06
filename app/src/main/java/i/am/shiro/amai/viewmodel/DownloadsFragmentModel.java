package i.am.shiro.amai.viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import i.am.shiro.amai.model.Book;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

import java.util.List;

public class DownloadsFragmentModel extends ViewModel {

    private final Realm realm = Realm.getDefaultInstance();

    private final MutableLiveData<List<Book>> books = new MutableLiveData<>();

    public DownloadsFragmentModel() {
        RealmResults<Book> offlineBooks = realm.where(Book.class)
                .equalTo("isDownloaded", true)
                .findAll();

        Timber.w("offline %s", offlineBooks.size());

        books.setValue(offlineBooks);
        offlineBooks.addChangeListener(books::setValue);
    }

    public void observeBooks(LifecycleOwner owner, Observer<List<Book>> observer) {
        books.observe(owner, observer);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        realm.close();
    }
}
