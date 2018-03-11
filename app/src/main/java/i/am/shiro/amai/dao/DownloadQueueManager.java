package i.am.shiro.amai.dao;

import java.io.Closeable;

import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.DownloadTask;
import io.realm.Realm;

/**
 * Created by Shiro on 3/11/2018.
 */

public class DownloadQueueManager implements Closeable {

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
}
