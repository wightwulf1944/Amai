package i.am.shiro.amai.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import i.am.shiro.amai.Preferences;
import i.am.shiro.amai.dao.SearchDao;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.network.Nhentai;
import i.am.shiro.amai.transformer.BookTransformer;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

public class NhentaiViewModel extends ViewModel {

    private static final int LOAD_MORE_THRESHHOLD = 10;

    private final MutableLiveData<List<Book>> books = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();

    private final SearchDao searchDao = new SearchDao();

    private Disposable disposable;

    public void onNewInstanceCreated() {
        String searchConstants = Preferences.getSearchConstants();
        searchDao.newSearch(searchConstants);
        books.setValue(searchDao.getResults());
        loadNextPage();
    }

    public void onPositionBind(int position) {
        if (searchDao.isBusy()) return;

        if (position > searchDao.getResultSize() - LOAD_MORE_THRESHHOLD) {
            loadNextPage();
        }
    }

    public void observeBooks(LifecycleOwner owner, Observer<List<Book>> observer) {
        books.observe(owner, observer);
    }

    public void observeLoadingState(LifecycleOwner owner, Observer<Boolean> observer) {
        loadingState.observe(owner, observer);
    }

    public void search(String query) {
        disposable.dispose();
        String searchConstants = Preferences.getSearchConstants();
        searchDao.newSearch(searchConstants + " " + query);
        books.setValue(searchDao.getResults());
        loadNextPage();
    }

    private void loadNextPage() {
        loadingState.setValue(true);
        searchDao.incrementCurrentPage();

        disposable = Nhentai.API.search(searchDao.getQuery(), searchDao.getCurrentPage(), null)
                .flattenAsObservable(bookSearchJson -> bookSearchJson.results)
                .map(BookTransformer::transform)
                .toList()
                .observeOn(mainThread())
                .subscribe(this::onBooksFetched, this::onFailed);
    }

    private void onBooksFetched(List<Book> newResults) {
        searchDao.appendResults(newResults);
        loadingState.setValue(false);
        books.setValue(searchDao.getResults());
    }

    private void onFailed(Throwable throwable) {
        searchDao.decrementCurrentPage();
        loadingState.setValue(false);
        Timber.w(throwable, "Failed to get data");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) disposable.dispose();
        searchDao.close();
    }
}
