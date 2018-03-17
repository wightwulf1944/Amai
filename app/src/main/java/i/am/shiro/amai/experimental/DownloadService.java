package i.am.shiro.amai.experimental;

import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.bumptech.glide.Glide;

import org.apache.commons.io.FileUtils;

import java.io.File;

import i.am.shiro.amai.R;
import i.am.shiro.amai.model.Book;

import static i.am.shiro.amai.constant.Constants.DEFAULT_CHANNEL_ID;

/**
 * Created by Shiro on 3/18/2018.
 */

public class DownloadService extends IntentService {

    private static final int NOTIFICATION_ID = 1;

    public static void start(Context context, Book book) {
        try (DownloadQueueManager queueManager = new DownloadQueueManager()) {
            queueManager.add(book);
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
        try (DownloadTaskDispatcher dispatcher = new DownloadTaskDispatcher()) {
            for (DownloadTask2 task : dispatcher) {
                dispatcher.notifyRunning(task);
                try {
                    runTask(task);
                    dispatcher.notifyDone(task);
                } catch (Exception e) {
                    dispatcher.notifyFailed(task);
                }
            }
        }
    }

    private void runTask(DownloadTask2 task) throws Exception {
        String sourceUrl = task.getSourceUrl();
        File sourceFile = downloadPage(sourceUrl);

        String destinationUrl = task.getDestinationUrl();
        File destinationFile = new File(destinationUrl);

        FileUtils.copyFile(sourceFile, destinationFile);
    }

    private File downloadPage(String pageUrl) throws Exception {
        return Glide.with(this)
                .download(pageUrl)
                .submit()
                .get();
    }
}
