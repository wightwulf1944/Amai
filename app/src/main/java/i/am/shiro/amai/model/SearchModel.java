package i.am.shiro.amai.model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class SearchModel extends RealmObject {

    private RealmList<Book> results;

    private int currentPage;

    private boolean isCompleted;

    private String query;

    public RealmList<Book> getResults() {
        return results;
    }

    public void setResults(RealmList<Book> results) {
        this.results = results;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
