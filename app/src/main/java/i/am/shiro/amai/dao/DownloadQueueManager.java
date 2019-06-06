package i.am.shiro.amai.dao;

import i.am.shiro.amai.Preferences;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.DownloadJob;
import i.am.shiro.amai.model.DownloadTask;
import i.am.shiro.amai.model.Image;
import io.realm.Realm;
import io.realm.RealmList;
import org.apache.commons.io.FilenameUtils;

import java.io.Closeable;

import static i.am.shiro.amai.constant.DownloadStatus.QUEUED;
import static java.lang.String.valueOf;

public class DownloadQueueManager implements Closeable {

    private final Realm realm = Realm.getDefaultInstance();

    @Override
    public void close() {
        realm.close();
    }

    public void add(Book book) {
        DownloadJob job = new DownloadJob();
        job.setParentBook(book);
        job.setBookId(book.getId());
        job.setTitle(book.getTitle());
        job.setStatus(QUEUED);
        job.setTaskList(makeTaskList(book, job));

        realm.beginTransaction();
        realm.insertOrUpdate(job);
        realm.commitTransaction();
    }

    private RealmList<DownloadTask> makeTaskList(Book book, DownloadJob job) {
        String storagePath = Preferences.getStoragePath();
        String bookPath = FilenameUtils.concat(storagePath, valueOf(book.getId()));

        RealmList<DownloadTask> taskList = new RealmList<>();
        for (Image image : book.getPageImages()) {
            String sourceUrl = image.getUrl();
            String filename = FilenameUtils.getName(sourceUrl);
            String destinationUrl = FilenameUtils.concat(bookPath, filename);

            DownloadTask task = new DownloadTask();
            task.setParentJob(job);
            task.setSourceUrl(sourceUrl);
            task.setDestinationUrl(destinationUrl);

            taskList.add(task);
        }

        return taskList;
    }
}
