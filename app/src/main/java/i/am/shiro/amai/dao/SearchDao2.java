package i.am.shiro.amai.dao;

import java.io.Closeable;
import java.util.List;

import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.SearchModel;
import io.realm.Realm;
import io.realm.RealmList;

public class SearchDao2 implements Closeable {

    private final Realm realm = Realm.getDefaultInstance();

    @Override
    public void close() {
        realm.close();
    }

    public SearchModel newSearch(String query) {
        SearchModel searchModel = new SearchModel();
        searchModel.setQuery(query);
        searchModel.setResults(new RealmList<>());
        return searchModel;
    }

    public SearchModel loadSearch() {
        SearchModel managedSearchModel = realm.where(SearchModel.class).findFirst();
        if (managedSearchModel == null) throw new IllegalStateException();
        return realm.copyFromRealm(managedSearchModel);
    }

    public void saveSearch(SearchModel searchModel) {
        realm.beginTransaction();
        realm.where(SearchModel.class).findAll().deleteAllFromRealm();
        realm.insert(searchModel);
        realm.commitTransaction();
    }

    public void updateBookDB(List<Book> newBooks) {
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
