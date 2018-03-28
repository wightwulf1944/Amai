package i.am.shiro.amai.viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

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

    private final SearchDao searchDao = new SearchDao();

    private final MutableLiveData<List<Book>> books = new MutableLiveData<>();

    private SearchModel search;

    private Disposable disposable;

    public BrowseFragmentModel2() {
        setSearch(searchDao.getDefaultSearch());
        loadSearchNextPage();
    }

    private void setSearch(SearchModel newSearch) {
        // TODO is this enough to remove listeners?
        search.removeAllChangeListeners();
        search = newSearch;
        RealmList<Book> searchResults = search.getResults();
        searchResults.addChangeListener(books::setValue);
        books.setValue(searchResults);
    }

    public void observeBooks(LifecycleOwner owner, Observer<List<Book>> observer) {
        books.observe(owner, observer);
    }

    public void newSearch(String query) {
        disposable.dispose();
        setSearch(searchDao.getSearch(query));
        loadSearchNextPage();
    }

    public void clearSearch() {
        disposable.dispose();
        setSearch(searchDao.getDefaultSearch());
        loadSearchNextPage();
    }

    public void onPositionBind(int position) {
        if (search.isLoading() || search.isCompleted()) return;

        int threshhold = 10;
        if (position > search.getResults().size() - threshhold) {
            loadSearchNextPage();
        }
    }

    private void loadSearchNextPage() {
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
