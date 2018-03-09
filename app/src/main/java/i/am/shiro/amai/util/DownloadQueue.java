package i.am.shiro.amai.util;

import android.support.annotation.NonNull;

import java.io.Closeable;
import java.util.Iterator;

import i.am.shiro.amai.constant.DownloadStatus;
import i.am.shiro.amai.model.Book;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

import static i.am.shiro.amai.constant.DownloadStatus.DONE;
import static i.am.shiro.amai.constant.DownloadStatus.FAILED;
import static i.am.shiro.amai.constant.DownloadStatus.QUEUED;


/**
 * Created by Shiro on 3/10/2018.
 * TODO: allow a book to fail up to 3x before giving up
 */

public class DownloadQueue implements Closeable, Iterable<Book> {

    private final Realm realm;

    private final RealmResults<Book> queue;

    public DownloadQueue() {
        realm = Realm.getDefaultInstance();
        queue = realm.where(Book.class)
                .equalTo("downloadStatus", QUEUED)
                .findAll();

        Timber.w("queue size: %s", queue.size());
    }

    @Override
    public void close() {
        realm.close();
    }

    @NonNull
    @Override
    public Iterator<Book> iterator() {
        return queue.iterator();
    }

    public void notifyQueued(Book book) {
        setBookDownloadStatus(book, QUEUED);
    }

    public void notifyDone(Book book) {
        setBookDownloadStatus(book, DONE);

        // FIXME
        realm.beginTransaction();
        book.setDownloaded(true);
        realm.commitTransaction();
    }

    public void notifyFailed(Book book) {
        setBookDownloadStatus(book, FAILED);
    }

    private void setBookDownloadStatus(Book book, @DownloadStatus int status) {
        realm.beginTransaction();
        book.setDownloadStatus(status);
        realm.commitTransaction();
    }
}
