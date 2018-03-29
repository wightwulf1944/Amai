package i.am.shiro.amai.viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import i.am.shiro.amai.dao.SearchDao;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.SearchModel;
import i.am.shiro.amai.retrofit.Nhentai;
import io.reactivex.disposables.Disposable;
import io.realm.RealmList;
import timber.log.Timber;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

/**
 * Created by Shiro on 1/20/2018.
 */

public class BrowseFragmentModel2 extends ViewModel {

    private static final int LOAD_MORE_THRESHHOLD = 10;

    private final SearchDao searchDao = new SearchDao();

    private final MutableLiveData<List<Book>> books = new MutableLiveData<>();

    private SearchModel search;

    private Disposable disposable;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (search != null) return;

        if (savedInstanceState == null) {
            search = searchDao.newSearch("language:english");
        } else {
            search = searchDao.getSearch();
        }
        updateBooks();
        loadNextPage();
    }

    public void observeBooks(LifecycleOwner owner, Observer<List<Book>> observer) {
        books.observe(owner, observer);
    }

    public void newSearch(String query) {
        disposable.dispose();
        search.getResults().removeAllChangeListeners();
        search = searchDao.newSearch("language:english " + query);
        updateBooks();
        loadNextPage();
    }

    public void onPositionBind(int position) {
        if (search.isLoading() || search.isCompleted()) return;

        if (position > search.getResults().size() - LOAD_MORE_THRESHHOLD) {
            loadNextPage();
        }
    }

    private void updateBooks() {
        RealmList<Book> results = search.getResults();
        results.addChangeListener(this::notifyBookObserver);
        notifyBookObserver(results);
    }

    private void notifyBookObserver(RealmList<Book> update) {
        ArrayList<Book> list = new ArrayList<>(update);
        books.setValue(list);
    }

    private void loadNextPage() {
        searchDao.onStartLoading(search);
        String query = search.getQuery();
        int currentPage = search.getCurrentPage();

        disposable = Nhentai.api.search(query, currentPage, null)
                .map(bookSearchJson -> bookSearchJson.results)
                .observeOn(mainThread())
                .subscribe(this::onBooksFetched, this::onFailed);
    }

    private void onBooksFetched(List<Book> fetchedBooks) {
        searchDao.onFinishLoading(search, fetchedBooks);
    }

    private void onFailed(Throwable throwable) {
        searchDao.onErrorLoading(search);
        Timber.w(throwable, "Failed to get data");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
        searchDao.close();
    }
}
