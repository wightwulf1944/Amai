package i.am.shiro.amai.viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import java.util.Collections;
import java.util.List;

import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.retrofit.Nhentai;
import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by Shiro on 1/20/2018.
 */

public class SourceFragmentViewModel extends ViewModel {

    private final MutableLiveData<List<Book>> books = new MutableLiveData<>();

    public SourceFragmentViewModel() {
        books.setValue(Collections.emptyList());
    }

    public List<Book> getBooks() {
        return books.getValue();
    }

    public void observeBooks(LifecycleOwner owner, Observer<List<Book>> observer) {
        books.observe(owner, observer);
    }

    public void fetchBooks() {
        Nhentai.api.getAll(1)
                .flattenAsObservable(bookSearchJson -> bookSearchJson.results)
                .map(Book::new)
                .toList()
                .subscribe(
                        this::onBooksFetched,
                        throwable -> Timber.d("Failed to get data", throwable)
                );
    }

    private void onBooksFetched(List<Book> fetchedBooks) {
        books.postValue(fetchedBooks);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(fetchedBooks);
        realm.commitTransaction();
        realm.close();
    }
}
