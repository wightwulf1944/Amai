package i.am.shiro.amai.viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import java.util.Collections;
import java.util.List;

import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.retrofit.Nhentai;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import timber.log.Timber;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

/**
 * Created by Shiro on 1/20/2018.
 */

public class BrowseFragmentModel extends ViewModel {

    private final Realm realm = Realm.getDefaultInstance();

    private final MutableLiveData<List<Book>> books = new MutableLiveData<>();

    private Disposable disposable;

    public BrowseFragmentModel() {
        books.setValue(Collections.emptyList());
    }

    public List<Book> getBooks() {
        return books.getValue();
    }

    public void observeBooks(LifecycleOwner owner, Observer<List<Book>> observer) {
        books.observe(owner, observer);
    }

    public void fetchBooks() {
        cancel();
        disposable = Nhentai.api.getAll(1)
                .flattenAsObservable(bookSearchJson -> bookSearchJson.results)
                .toList()
                .observeOn(mainThread())
                .subscribe(
                        this::onBooksFetched,
                        throwable -> Timber.w("Failed to get data", throwable)
                );
    }

    public void search(String query) {
        cancel();
        disposable = Nhentai.api.search(query, 1, null)
                .flattenAsObservable(bookSearchJson -> bookSearchJson.results)
                .toList()
                .observeOn(mainThread())
                .subscribe(
                        this::onBooksFetched,
                        throwable -> Timber.w("Failed to get data", throwable)
                );
    }

    private void cancel() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void onBooksFetched(List<Book> fetchedBooks) {
        books.setValue(fetchedBooks);
        realm.beginTransaction();
        for (Book fetchedBook : fetchedBooks) mergeBook(fetchedBook);
        realm.commitTransaction();
    }

    private void mergeBook(Book remoteBook) {
        Book localBook = realm.where(Book.class)
                .equalTo("id", remoteBook.getId())
                .findFirst();

        if (localBook == null) {
            realm.insert(remoteBook);
        } else {
            localBook.mergeWith(remoteBook);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) disposable.dispose();
        realm.close();
    }
}
