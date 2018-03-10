package i.am.shiro.amai.util;

import java.io.Closeable;

import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.DownloadTask;
import io.realm.Realm;

import static i.am.shiro.amai.constant.DownloadStatus.DONE;
import static i.am.shiro.amai.constant.DownloadStatus.FAILED;
import static i.am.shiro.amai.constant.DownloadStatus.QUEUED;
import static i.am.shiro.amai.constant.DownloadStatus.RUNNING;

/**
 * Created by Shiro on 3/10/2018.
 */

public class DownloadManager implements Closeable {

    private static final int MAX_TRIES = 3;

    private final Realm realm = Realm.getDefaultInstance();

    @Override
    public void close() {
        realm.close();
    }

    public void addToQueue(Book book) {
        realm.beginTransaction();
        DownloadTask task = realm.createObject(DownloadTask.class);
        task.setBook(book);
        realm.commitTransaction();
    }

    public Iterable<DownloadTask> getQueued() {
        return realm.where(DownloadTask.class)
                .equalTo("status", QUEUED)
                .findAll();
    }

    public void notifyRunning(DownloadTask task) {
        realm.beginTransaction();
        task.setStatus(RUNNING);
        realm.commitTransaction();
    }

    public void notifyDone(DownloadTask task) {
        realm.beginTransaction();
        task.setStatus(DONE);

        // FIXME
        task.getBook().setDownloaded(true);
        realm.commitTransaction();
    }

    public void notifyFailed(DownloadTask task) {
        realm.beginTransaction();
        if (task.getTries() < MAX_TRIES) {
            task.incrementTries();
            task.setStatus(QUEUED);
        } else {
            task.setStatus(FAILED);
        }
        realm.commitTransaction();
    }
}
