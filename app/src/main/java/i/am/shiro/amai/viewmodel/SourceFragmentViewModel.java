package i.am.shiro.amai.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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

    private final Realm realm = Realm.getDefaultInstance();

    private final MutableLiveData<List<Book>> books = new MutableLiveData<>();

    public SourceFragmentViewModel() {
        books.setValue(Collections.emptyList());
    }

    public LiveData<List<Book>> getBooks() {
        return books;
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
        realm.insertOrUpdate(fetchedBooks);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        realm.close();
    }
}
