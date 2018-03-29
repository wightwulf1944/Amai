package i.am.shiro.amai.dao;

import java.io.Closeable;
import java.util.HashSet;
import java.util.List;

import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.SearchModel;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Shiro on 3/26/2018.
 */

public class SearchDao implements Closeable {

    private final Realm realm = Realm.getDefaultInstance();

    @Override
    public void close() {
        realm.close();
    }

    public SearchModel newSearch(String query) {
        realm.beginTransaction();

        realm.where(SearchModel.class)
                .findAll()
                .deleteAllFromRealm();

        SearchModel searchModel = realm.createObject(SearchModel.class);
        searchModel.setQuery(query);

        realm.commitTransaction();
        return searchModel;
    }

    public SearchModel getSearch() {
        return realm.where(SearchModel.class).findFirst();
    }

    public void onStartLoading(SearchModel searchModel) {
        realm.beginTransaction();
        searchModel.setLoading(true);
        searchModel.setCurrentPage(searchModel.getCurrentPage() + 1);
        realm.commitTransaction();
    }

    public void onFinishLoading(SearchModel searchModel, List<Book> newResults) {
        realm.beginTransaction();

        updateBookDB(newResults);

        RealmList<Book> results = searchModel.getResults();
        HashSet<Book> resultsSet = new HashSet<>(results);
        for (Book newResult : newResults) {
            if (!resultsSet.contains(newResult)) {
                results.add(newResult);
            }
        }

        searchModel.setLoading(false);

        realm.commitTransaction();
    }

    public void onErrorLoading(SearchModel searchModel) {
        realm.beginTransaction();
        searchModel.setCurrentPage(searchModel.getCurrentPage() - 1);
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
}
