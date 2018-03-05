package i.am.shiro.amai.viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import i.am.shiro.amai.constant.BookStatus;
import i.am.shiro.amai.model.Book;
import io.realm.OrderedCollectionChangeSet;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

import static i.am.shiro.amai.constant.BookStatus.OFFLINE;
import static i.am.shiro.amai.constant.BookStatus.ONLINE;
import static i.am.shiro.amai.constant.BookStatus.QUEUED;

/**
 * Created by Shiro on 2/21/2018.
 */

public class DownloadsFragmentModel extends ViewModel {

    private final Realm realm = Realm.getDefaultInstance();

    private final MutableLiveData<List<Book>> books = new MutableLiveData<>();


    public DownloadsFragmentModel() {
        RealmResults<Book> offlineBooks = realm.where(Book.class)
                .equalTo("status", OFFLINE)
                .findAll();

        Timber.w("offline %s", offlineBooks.size());
//
//
//        RealmResults<Book> queuedBooks = realm.where(Book.class)
//                .equalTo("status", QUEUED)
//                .findAll();
//
//        Timber.w("queued %s", queuedBooks.size());
//
//
//        RealmResults<Book> onlineBooks = realm.where(Book.class)
//                .equalTo("status", ONLINE)
//                .findAll();
//
//        Timber.w("online %s", onlineBooks.size());

        books.setValue(offlineBooks);

        offlineBooks.addChangeListener(this::onBooksUpdated);
    }

    public void observeBooks(LifecycleOwner owner, Observer<List<Book>> observer) {
        books.observe(owner, observer);
    }

    private void onBooksUpdated(List<Book> books, OrderedCollectionChangeSet changeSet) {
        // todo
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        realm.close();
    }
}
