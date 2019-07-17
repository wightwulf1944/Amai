package i.am.shiro.amai.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import org.apache.commons.io.FileUtils;

import java.io.File;

import i.am.shiro.amai.dao.DownloadQueueManager;
import i.am.shiro.amai.dao.DownloadTaskDispatcher;
import i.am.shiro.amai.model.Book;
import i.am.shiro.amai.model.DownloadTask;
import i.am.shiro.amai.notification.ProgressNotice;
import i.am.shiro.lib.notification.ServiceNotifier;
import timber.log.Timber;

public class DownloadService extends IntentService {

    private final ServiceNotifier notifier = new ServiceNotifier(this, 1);

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
        notifier.startForeground(new ProgressNotice());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try (DownloadTaskDispatcher dispatcher = new DownloadTaskDispatcher()) {
            for (DownloadTask task : dispatcher) {
                dispatcher.notifyRunning(task);
                try {
                    runTask(task);
                    dispatcher.notifyDone(task);
                } catch (Exception e) {
                    Timber.w(e);
                    dispatcher.notifyFailed(task);
                }
            }
        }
    }

    private void runTask(DownloadTask task) throws Exception {
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
