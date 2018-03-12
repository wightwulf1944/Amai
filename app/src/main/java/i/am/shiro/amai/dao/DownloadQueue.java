package i.am.shiro.amai.dao;

import android.support.annotation.NonNull;

import java.io.Closeable;
import java.util.Iterator;

import i.am.shiro.amai.model.DownloadTask;
import io.realm.Realm;

import static i.am.shiro.amai.constant.DownloadStatus.DONE;
import static i.am.shiro.amai.constant.DownloadStatus.FAILED;
import static i.am.shiro.amai.constant.DownloadStatus.QUEUED;
import static i.am.shiro.amai.constant.DownloadStatus.RUNNING;

/**
 * Created by Shiro on 3/10/2018.
 * responsible for maintaining the download queue for DownloadService
 * also acts as a Realm DAO to reduce Realm code in DownloadService
 * methods to manage the queue is implemented by DownloadQueueManager
 *
 * TODO:
 * possibly should implement Iterator
 */

public class DownloadQueue implements Closeable, Iterable<DownloadTask> {

    private static final int MAX_TRIES = 3;

    private final Realm realm = Realm.getDefaultInstance();

    @Override
    public void close() {
        realm.close();
    }

    @NonNull
    @Override
    public Iterator<DownloadTask> iterator() {
        return realm.where(DownloadTask.class)
                .equalTo("status", QUEUED)
                .or()
                .equalTo("status", RUNNING)
                .findAll()
                .iterator();
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
