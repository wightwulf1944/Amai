package i.am.shiro.amai.util;

import java.io.Closeable;

import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.DownloadTask;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

import static i.am.shiro.amai.constant.DownloadStatus.DONE;
import static i.am.shiro.amai.constant.DownloadStatus.FAILED;
import static i.am.shiro.amai.constant.DownloadStatus.QUEUED;
import static i.am.shiro.amai.constant.DownloadStatus.RUNNING;

/**
 * Created by Shiro on 3/10/2018.
 */

public class DownloadManager implements Closeable {

    private static final int MAX_TRIES = 3;

    private final Realm realm;

    private final RealmResults<DownloadTask> queue;

    public DownloadManager() {
        realm = Realm.getDefaultInstance();
        queue = realm.where(DownloadTask.class)
                .equalTo("status", QUEUED)
                .findAll();

        Timber.w("queue size: %s", queue.size());
    }

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

    public Iterable<DownloadTask> getQueue() {
        return queue;
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
