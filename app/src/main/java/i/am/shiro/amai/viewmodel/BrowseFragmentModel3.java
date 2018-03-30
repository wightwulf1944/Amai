package i.am.shiro.amai.viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.HashSet;
import java.util.List;

import i.am.shiro.amai.dao.SearchDao2;
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

public class BrowseFragmentModel3 extends ViewModel {

    private static final int LOAD_MORE_THRESHHOLD = 10;

    private final SearchDao2 searchDao = new SearchDao2();

    private final MutableLiveData<List<Book>> books = new MutableLiveData<>();

    private SearchModel search;

    private Disposable disposable;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (search != null) return;

        if (savedInstanceState == null) {
            search = searchDao.newSearch("language:english");
        } else {
            search = searchDao.loadSearch();
        }
        books.setValue(search.getResults());
        loadNextPage();
    }

    public void onPause() {
        searchDao.saveSearch(search);
    }

    public void onPositionBind(int position) {
        if (search.isCompleted() || search.isLoading()) return;

        if (position > search.getResults().size() - LOAD_MORE_THRESHHOLD) {
            loadNextPage();
        }
    }

    public void observeBooks(LifecycleOwner owner, Observer<List<Book>> observer) {
        books.observe(owner, observer);
    }

    public void search(String query) {
        disposable.dispose();
        search = searchDao.newSearch("langiage:english " + query);
        books.setValue(search.getResults());
        loadNextPage();
    }

    private void loadNextPage() {
        search.setLoading(true);
        search.setCurrentPage(search.getCurrentPage() + 1);

        disposable = Nhentai.api.search(search.getQuery(), search.getCurrentPage(), null)
                .map(bookSearchJson -> bookSearchJson.results)
                .observeOn(mainThread())
                .subscribe(this::onBooksFetched, this::onFailed);
    }

    private void onBooksFetched(List<Book> newResults) {
        searchDao.updateBookDB(newResults);

        RealmList<Book> results = search.getResults();
        HashSet<Book> resultsSet = new HashSet<>(results);
        for (Book newResult : newResults) {
            if (!resultsSet.contains(newResult)) {
                results.add(newResult);
            }
        }

        search.setLoading(false);
    }

    private void onFailed(Throwable throwable) {
        search.setCurrentPage(search.getCurrentPage() - 1);
        search.setLoading(false);
        Timber.w(throwable, "Failed to get data");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
        searchDao.close();
    }
}
