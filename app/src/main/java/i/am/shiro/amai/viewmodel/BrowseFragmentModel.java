package i.am.shiro.amai.viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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

    private int currentPage;

    private boolean isLoading;

    private boolean isLastPageLoaded;

    private String query;

    public BrowseFragmentModel() {
        books.setValue(Collections.emptyList());
        loadAllNextPage();
    }

    public void observeBooks(LifecycleOwner owner, Observer<List<Book>> observer) {
        books.observe(owner, observer);
    }

    public void newSearch(String query) {
        disposable.dispose();
        books.setValue(Collections.emptyList());
        currentPage = 0;
        isLastPageLoaded = false;
        this.query = query;
        loadSearchNextPage();
    }

    public void clearSearch() {
        disposable.dispose();
        books.setValue(Collections.emptyList());
        currentPage = 0;
        isLastPageLoaded = false;
        this.query = null;
        loadAllNextPage();
    }

    public void onPositionBind(int position) {
        if (isLastPageLoaded || isLoading) return;

        List<Book> loadedBooks = books.getValue();
        int threshhold = 10;
        if (position > loadedBooks.size() - threshhold) {
            isLoading = true;
            if (query == null) {
                loadAllNextPage();
            } else {
                loadSearchNextPage();
            }
        }
    }

    private void loadSearchNextPage() {
        currentPage = ++currentPage;
        disposable = Nhentai.api.search(query, currentPage, null)
                .map(bookSearchJson -> bookSearchJson.results)
                .observeOn(mainThread())
                .subscribe(
                        this::onBooksFetched,
                        throwable -> Timber.w(throwable, "Failed to get data")
                );
    }

    private void loadAllNextPage() {
        currentPage = ++currentPage;
        disposable = Nhentai.api.getAll(currentPage)
                .map(bookSearchJson -> bookSearchJson.results)
                .observeOn(mainThread())
                .subscribe(
                        this::onBooksFetched,
                        throwable -> Timber.w(throwable, "Failed to get data")
                );
    }

    private void onBooksFetched(List<Book> fetchedBooks) {
        List<Book> oldBooksList = books.getValue();
        List<Book> newBooksList = mergeBookList(oldBooksList, fetchedBooks);
        books.setValue(newBooksList);

        realm.beginTransaction();
        for (Book fetchedBook : fetchedBooks) updateBookDB(fetchedBook);
        realm.commitTransaction();

        isLoading = false;
    }

    private List<Book> mergeBookList(List<Book> listA, List<Book> listB) {
        HashSet<Book> listASet = new HashSet<>(listA);

        List<Book> mergedBookList = new ArrayList<>(listA);
        for (Book book : listB) {
            if (!listASet.contains(book)) {
                mergedBookList.add(book);
            }
        }

        return mergedBookList;
    }

    private void updateBookDB(Book remoteBook) {
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
        disposable.dispose();
        realm.close();
    }
}
