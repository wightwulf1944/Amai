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

import i.am.shiro.amai.Preferences;
import i.am.shiro.amai.R;
import i.am.shiro.amai.dao.DownloadQueue;
import i.am.shiro.amai.dao.DownloadQueueManager;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.DownloadTask;
import i.am.shiro.amai.model.Image;
import timber.log.Timber;

import static i.am.shiro.amai.constant.Constants.DEFAULT_CHANNEL_ID;

/**
 * Created by Shiro on 2/20/2018.
 * TODO: create companion notification
 */

public class DownloadService extends IntentService {

    private static final int NOTIFICATION_ID = 1;

    public static void start(Context context, Book book) {
        try (DownloadQueueManager queueManager = new DownloadQueueManager()) {
            queueManager.addToQueue(book);
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
        DownloadQueue downloadQueue = new DownloadQueue();

        for (DownloadTask task : downloadQueue) {
            downloadQueue.notifyRunning(task);
            Book book = task.getBook();
            try {
                downloadBook(book);
                downloadQueue.notifyDone(task);
                Timber.w("Book successfully downloaded: %s", book.getId());
            } catch (Exception e) {
                downloadQueue.notifyFailed(task);
                Timber.w(e, "Failed to download book: %s", book.getId());
            }
        }

        downloadQueue.close();
    }

    private void downloadBook(Book book) throws Exception {
        File bookDir = getBookDir(book);
        for (Image pageImage : book.getPageImages()) {
            String pageImageUrl = pageImage.getUrl();
            File srcPageFile = downloadPage(pageImageUrl);
            File destPageFile = getPageFile(bookDir, pageImageUrl);
            FileUtils.copyFile(srcPageFile, destPageFile);
            Timber.w("Page successfully downloaded: %s", pageImageUrl);
        }
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
