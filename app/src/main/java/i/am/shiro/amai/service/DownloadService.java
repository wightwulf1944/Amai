package i.am.shiro.amai.service;

import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.bumptech.glide.Glide;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;

import i.am.shiro.amai.Preferences;
import i.am.shiro.amai.R;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.Image;
import io.realm.Realm;
import timber.log.Timber;

import static i.am.shiro.amai.constant.BookStatus.OFFLINE;
import static i.am.shiro.amai.constant.BookStatus.QUEUED;
import static i.am.shiro.amai.constant.Constants.DEFAULT_CHANNEL_ID;

/**
 * Created by Shiro on 2/20/2018.
 * TODO: create companion notification
 */

public class DownloadService extends IntentService {

    private static final int NOTIFICATION_ID = 1;

    public static void start(Context context, Book book) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            book.setStatus(QUEUED);
            realm.commitTransaction();
        }

        Intent intent = new Intent(context, DownloadService.class);
        context.startService(intent);
    }

    public DownloadService() {
        super(DownloadService.class.getSimpleName());
        setIntentRedelivery(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Notification notification = new NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle("Downloading")
                .setProgress(0, 0, true)
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Realm realm = Realm.getDefaultInstance();
        for (Book book : getQueue(realm)) {
            try {
                downloadBook(realm, book);
                Timber.w("Book successfully downloaded: %s", book.getId());
            } catch (Exception e) {
                Timber.w(e, "Failed to download book: %s", book.getId());
            }
        }
        realm.close();
    }

    private List<Book> getQueue(Realm realm) {
        return realm.where(Book.class)
                .equalTo("status", QUEUED)
                .findAll();
    }

    private void downloadBook(Realm realm, Book book) throws Exception {
        File bookDir = getBookDir(book);
        for (Image pageImage : book.getPageImages()) {
            String pageImageUrl = pageImage.getUrl();
            File srcPageFile = downloadPage(pageImageUrl);
            File destPageFile = getPageFile(bookDir, pageImageUrl);
            FileUtils.copyFile(srcPageFile, destPageFile);
            Timber.w("Page successfully downloaded: %s", pageImageUrl);
        }

        realm.beginTransaction();
        book.setStatus(OFFLINE);
        realm.commitTransaction();
    }

    private File getBookDir(Book book) {
        String storagePath = Preferences.getStoragePath();
        File storageDir = new File(storagePath);
        String dirName = String.valueOf(book.getId());
        return new File(storageDir, dirName);
    }

    private File downloadPage(String pageUrl) throws Exception {
        return Glide.with(this)
                .download(pageUrl)
                .submit()
                .get();
    }

    private File getPageFile(File bookDir, String pageUrl) {
        String pageFilePath = FilenameUtils.getName(pageUrl);
        return new File(bookDir, pageFilePath);
    }
}
