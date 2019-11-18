package i.am.shiro.amai.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

    private final MutableLiveData<List<Book>> booksLive = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isLoadingLive = new MutableLiveData<>();

    private final SearchDao searchDao = new SearchDao();

    private Disposable disposable;

    public void onNewInstanceCreated() {
        String searchConstants = Preferences.getSearchConstants();
        searchDao.newSearch(searchConstants);
        booksLive.setValue(searchDao.getResults());
        loadNextPage();
    }

    public void onPositionBind(int position) {
        if (searchDao.isBusy()) return;

        if (position > searchDao.getResultSize() - LOAD_MORE_THRESHHOLD) {
            loadNextPage();
        }
    }

    public LiveData<List<Book>> getBooksLive() {
        return booksLive;
    }

    public LiveData<Boolean> getIsLoadingLive() {
        return isLoadingLive;
    }

    public void search(String query) {
        disposable.dispose();
        String searchConstants = Preferences.getSearchConstants();
        searchDao.newSearch(searchConstants + " " + query);
        booksLive.setValue(searchDao.getResults());
        loadNextPage();
    }

    private void loadNextPage() {
        isLoadingLive.setValue(true);
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
        isLoadingLive.setValue(false);
        booksLive.setValue(searchDao.getResults());
    }

    private void onFailed(Throwable throwable) {
        searchDao.decrementCurrentPage();
        isLoadingLive.setValue(false);
        Timber.w(throwable, "Failed to get data");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) disposable.dispose();
        searchDao.close();
    }
}
