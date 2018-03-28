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

    public SearchModel getDefaultSearch() {
        realm.beginTransaction();
        SearchModel defaultSearch = realm.createObject(SearchModel.class);
        realm.commitTransaction();
        return defaultSearch;
    }

    public SearchModel getSearch(String query) {
        realm.beginTransaction();
        SearchModel search = realm.createObject(SearchModel.class);
        search.setQuery(query);
        realm.commitTransaction();
        return search;
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

        RealmList<Book> oldResults = searchModel.getResults();
        RealmList<Book> mergedResults = mergeBookList(oldResults, newResults);
        searchModel.setResults(mergedResults);

        searchModel.setLoading(false);

        realm.commitTransaction();
    }

    public void onErrorLoading(SearchModel searchModel) {
        realm.beginTransaction();
        searchModel.setCurrentPage(searchModel.getCurrentPage() - 1);
        searchModel.setLoading(false);
        realm.commitTransaction();
    }

    private RealmList<Book> mergeBookList(List<Book> listA, List<Book> listB) {
        HashSet<Book> listASet = new HashSet<>(listA);

        RealmList<Book> mergedBookList = new RealmList<>();
        mergedBookList.addAll(listA);
        for (Book book : listB) {
            if (!listASet.contains(book)) {
                mergedBookList.add(book);
            }
        }

        return mergedBookList;
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
