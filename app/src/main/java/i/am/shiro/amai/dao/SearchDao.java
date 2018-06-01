package i.am.shiro.amai.dao;

import java.io.Closeable;
import java.util.HashSet;
import java.util.List;

import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.SearchModel;
import io.realm.Realm;
import io.realm.RealmList;

public class SearchDao implements Closeable {

    private final Realm realm = Realm.getDefaultInstance();

    private SearchModel searchModel;

    public SearchDao() {
        searchModel = realm.where(SearchModel.class).findFirst();
    }

    public void newSearch(String query) {
        realm.beginTransaction();

        realm.where(SearchModel.class).findAll().deleteAllFromRealm();

        searchModel = realm.createObject(SearchModel.class);
        searchModel.setQuery(query);

        realm.commitTransaction();
    }

    public String getQuery() {
        return searchModel.getQuery();
    }

    public List<Book> getResults() {
        return realm.copyFromRealm(searchModel.getResults());
    }

    public int getResultSize() {
        return searchModel.getResults().size();
    }

    public void appendResults(List<Book> newResults) {
        realm.beginTransaction();
        updateBookDB(newResults);

        RealmList<Book> results = searchModel.getResults();
        HashSet<Book> resultsSet = new HashSet<>(results);
        for (Book newResult : newResults) {
            if (!resultsSet.contains(newResult)) {
                results.add(newResult);
            }
        }

        realm.commitTransaction();
    }

    public int getCurrentPage() {
        return searchModel.getCurrentPage();
    }

    public void incrementCurrentPage() {
        realm.beginTransaction();
        searchModel.setCurrentPage(searchModel.getCurrentPage() + 1);
        realm.commitTransaction();
    }

    public void decrementCurrentPage() {
        realm.beginTransaction();
        searchModel.setCurrentPage(searchModel.getCurrentPage() - 1);
        realm.commitTransaction();
    }

    public boolean isBusy() {
        return searchModel.isLoading() || searchModel.isCompleted();
    }

    public void notifyLoadingStart() {
        realm.beginTransaction();
        searchModel.setLoading(true);
        realm.commitTransaction();
    }

    public void notifyLoadingDone() {
        realm.beginTransaction();
        searchModel.setLoading(false);
        realm.commitTransaction();
    }

    private void updateBookDB(List<Book> newBooks) {
        for (Book newBook : newBooks) {
            Book localBook = realm.where(Book.class)
                    .equalTo("id", newBook.getId())
                    .findFirst();

            if (localBook == null) {
                realm.insert(newBook);
            } else {
                localBook.mergeWith(newBook);
            }
        }
    }

    @Override
    public void close() {
        realm.close();
    }
}