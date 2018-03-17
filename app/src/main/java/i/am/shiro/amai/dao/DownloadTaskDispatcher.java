package i.am.shiro.amai.dao;

import android.support.annotation.NonNull;

import java.io.Closeable;
import java.util.Iterator;

import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.DownloadJob;
import i.am.shiro.amai.model.DownloadTask;
import io.realm.Realm;
import timber.log.Timber;

import static i.am.shiro.amai.constant.DownloadStatus.DONE;
import static i.am.shiro.amai.constant.DownloadStatus.FAILED;
import static i.am.shiro.amai.constant.DownloadStatus.QUEUED;
import static i.am.shiro.amai.constant.DownloadStatus.RUNNING;

/**
 * Created by Shiro on 3/18/2018.
 */

public class DownloadTaskDispatcher implements Closeable, Iterable<DownloadTask> {

    private static final int MAX_TRIES = 3;

    private final Realm realm = Realm.getDefaultInstance();

    @Override
    public void close() {
        realm.close();
    }

    public void notifyRunning(DownloadTask task) {
        realm.beginTransaction();
        DownloadJob parentJob = task.getParentJob();
        parentJob.setStatus(RUNNING);
        realm.commitTransaction();
    }

    public void notifyDone(DownloadTask task) {
        realm.beginTransaction();
        DownloadJob parentJob = task.getParentJob();
        parentJob.incrementTaskIndex();
        Timber.w("page downloaded %s", task.getSourceUrl());

        if (parentJob.getTaskIndex() == parentJob.getTaskList().size()) {
            parentJob.setStatus(DONE);
            Book parentBook = parentJob.getParentBook();
            updateBookUrls(parentBook, parentJob);
            Timber.w("Book %s downloaded successfuly", parentJob.getBookId());
        }
        realm.commitTransaction();
    }

    public void notifyFailed(DownloadTask task) {
        realm.beginTransaction();
        DownloadJob parentJob = task.getParentJob();
        if (parentJob.getTries() < MAX_TRIES) {
            parentJob.incrementTries();
            parentJob.setStatus(QUEUED);
        } else {
            parentJob.setStatus(FAILED);
        }
        realm.commitTransaction();
    }

    private void updateBookUrls(Book book, DownloadJob job) {
        book.setDownloaded(true);

        // TODO
    }

    @NonNull
    @Override
    public QueueIterator iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<DownloadTask> {

        @Override
        public boolean hasNext() {
            DownloadJob job = realm.where(DownloadJob.class)
                    .equalTo("status", RUNNING)
                    .or()
                    .equalTo("status", QUEUED)
                    .findFirst();

            return job != null;
        }

        @Override
        public DownloadTask next() {
            DownloadJob job = realm.where(DownloadJob.class)
                    .equalTo("status", RUNNING)
                    .or()
                    .equalTo("status", QUEUED)
                    .findFirst();

            if (job == null) {
                throw new NullPointerException("next() was called even if there are no jobs");
            }

            return job.getCurrentTask();
        }
    }
}
