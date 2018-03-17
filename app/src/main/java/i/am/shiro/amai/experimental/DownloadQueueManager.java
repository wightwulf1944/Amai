package i.am.shiro.amai.experimental;

import org.apache.commons.io.FilenameUtils;

import java.io.Closeable;

import i.am.shiro.amai.Preferences;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.Image;
import io.realm.Realm;
import io.realm.RealmList;

import static i.am.shiro.amai.constant.DownloadStatus.QUEUED;
import static java.lang.String.valueOf;

/**
 * Created by Shiro on 3/17/2018.
 */

public class DownloadQueueManager implements Closeable {

    private final Realm realm = Realm.getDefaultInstance();

    @Override
    public void close() {
        realm.close();
    }

    public void add(Book book) {
        DownloadJob job = new DownloadJob();
        job.setBookId(book.getId());
        job.setTitle(book.getTitle());
        job.setStatus(QUEUED);
        job.setTaskList(makeTaskListFrom(book));
    }

    private RealmList<DownloadTask2> makeTaskListFrom(Book book) {
        String storagePath = Preferences.getStoragePath();
        String bookPath = FilenameUtils.concat(storagePath, valueOf(book.getId()));

        RealmList<DownloadTask2> taskList = new RealmList<>();
        for (Image image : book.getPageImages()) {
            String sourceUrl = image.getUrl();
            String filename = FilenameUtils.getName(sourceUrl);
            String destinationUrl = FilenameUtils.concat(bookPath, filename);

            DownloadTask2 task = new DownloadTask2();
            task.setSourceUrl(sourceUrl);
            task.setDestinationUrl(destinationUrl);

            taskList.add(task);
        }

        return taskList;
    }
}
