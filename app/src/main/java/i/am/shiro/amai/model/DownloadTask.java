package i.am.shiro.amai.model;

import i.am.shiro.amai.constant.DownloadStatus;
import io.realm.RealmObject;

/**
 * Created by Shiro on 3/10/2018.
 */

public class DownloadTask extends RealmObject {

    private Book book;

    private int status;

    private int tries;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @DownloadStatus
    public int getStatus() {
        return status;
    }

    public void setStatus(@DownloadStatus int status) {
        this.status = status;
    }

    public int getTries() {
        return tries;
    }

    public void incrementTries() {
        tries++;
    }

    public void resetTries() {
        tries = 0;
    }
}
