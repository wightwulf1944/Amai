package i.am.shiro.amai.dao;

import androidx.annotation.NonNull;

import java.io.Closeable;
import java.util.Iterator;

import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.DownloadJob;
import i.am.shiro.amai.model.DownloadTask;
import i.am.shiro.amai.model.Image;
import io.realm.Realm;
import io.realm.RealmList;
import timber.log.Timber;

import static i.am.shiro.amai.constant.DownloadStatus.DONE;
import static i.am.shiro.amai.constant.DownloadStatus.FAILED;
import static i.am.shiro.amai.constant.DownloadStatus.QUEUED;
import static i.am.shiro.amai.constant.DownloadStatus.RUNNING;

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
            updateBookUrls(parentJob);
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

    private void updateBookUrls(DownloadJob job) {
        RealmList<DownloadTask> taskList = job.getTaskList();
        Book parentBook = job.getParentBook();

        Image remoteCoverImage = parentBook.getCoverImage();
        Image localCoverImage = imageFrom(remoteCoverImage, taskList.get(0));

        RealmList<Image> remotePageImages = parentBook.getPageImages();
        RealmList<Image> localPageImages = new RealmList<>();
        for (int i = 0; i < remotePageImages.size(); i++) {
            Image remotePageImage = remotePageImages.get(i);
            DownloadTask pageTask = taskList.get(i);
            Image localPageImage = imageFrom(remotePageImage, pageTask);
            localPageImages.add(localPageImage);
        }

        parentBook.setDownloaded(true)
                .setLocalCoverImage(localCoverImage)
                .setLocalCoverThumbnailImage(localCoverImage)
                .setLocalPageImages(localPageImages)
                .setLocalPageThumbnailImages(localPageImages);
    }

    private Image imageFrom(Image from, DownloadTask task) {
        return realm.createObject(Image.class)
                .setHeight(from.getHeight())
                .setWidth(from.getWidth())
                .setUrl(task.getDestinationUrl());
    }

    @NonNull
    @Override
    public Iterator<DownloadTask> iterator() {
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
