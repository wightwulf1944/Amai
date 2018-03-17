package i.am.shiro.amai.experimental;

import android.support.annotation.NonNull;

import java.io.Closeable;
import java.util.Iterator;

import i.am.shiro.amai.model.Book;
import io.realm.Realm;

import static i.am.shiro.amai.constant.DownloadStatus.DONE;
import static i.am.shiro.amai.constant.DownloadStatus.FAILED;
import static i.am.shiro.amai.constant.DownloadStatus.QUEUED;
import static i.am.shiro.amai.constant.DownloadStatus.RUNNING;

/**
 * Created by Shiro on 3/18/2018.
 */

public class DownloadTaskDispatcher implements Closeable, Iterable<DownloadTask2> {

    private static final int MAX_TRIES = 3;

    private final Realm realm = Realm.getDefaultInstance();

    @Override
    public void close() {
        realm.close();
    }

    public void notifyRunning(DownloadTask2 task) {
        realm.beginTransaction();
        DownloadJob parentJob = task.getParentJob();
        parentJob.setStatus(RUNNING);
        realm.commitTransaction();
    }

    public void notifyDone(DownloadTask2 task) {
        realm.beginTransaction();
        DownloadJob parentJob = task.getParentJob();
        parentJob.incrementTaskIndex();

        if (parentJob.getTaskIndex() > parentJob.getTaskList().size()) {
            parentJob.setStatus(DONE);
            Book parentBook = parentJob.getParentBook();
            updateBook(parentBook, parentJob);
        }
        realm.commitTransaction();
    }

    public void notifyFailed(DownloadTask2 task) {
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

    private void updateBook(Book book, DownloadJob job) {
        book.setDownloaded(true);

        // TODO
    }

    @NonNull
    @Override
    public QueueIterator iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<DownloadTask2> {

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
        public DownloadTask2 next() {
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
