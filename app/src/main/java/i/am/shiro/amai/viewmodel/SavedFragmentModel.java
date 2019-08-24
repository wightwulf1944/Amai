package i.am.shiro.amai.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import i.am.shiro.amai.model.Book;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

public class SavedFragmentModel extends ViewModel {

    private final Realm realm = Realm.getDefaultInstance();

    private final MutableLiveData<List<Book>> books = new MutableLiveData<>();

    public SavedFragmentModel() {
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

    public void onBookDelete(int bookId) {
        realm.executeTransaction(r -> r.where(Book.class)
                .equalTo("id", bookId)
                .findAll()
                .deleteAllFromRealm());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        realm.close();
    }
}