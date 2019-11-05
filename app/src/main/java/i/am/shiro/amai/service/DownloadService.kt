package i.am.shiro.amai.service

import android.app.IntentService
import android.app.Notification
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import i.am.shiro.amai.R
import i.am.shiro.amai.constant.Constants
import i.am.shiro.amai.dao.DownloadQueueManager
import i.am.shiro.amai.dao.DownloadTaskDispatcher
import i.am.shiro.amai.model.Book
import i.am.shiro.amai.model.DownloadTask
import i.am.shiro.amai.util.startLocalService
import org.apache.commons.io.FileUtils
import timber.log.Timber
import java.io.File

fun Context.addToQueue(book: Book) {
    DownloadQueueManager().use { queueManager -> queueManager.add(book) }
    startLocalService<DownloadService>()
}

class DownloadService : IntentService(DownloadService::class.java.simpleName) {

    init {
        setIntentRedelivery(true)
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(1, progressNotification())
    }

    override fun onHandleIntent(intent: Intent?) {
        DownloadTaskDispatcher().use { dispatcher ->
            for (task in dispatcher) {
                dispatcher.notifyRunning(task)
                try {
                    runTask(task)
                    dispatcher.notifyDone(task)
                } catch (e: Exception) {
                    Timber.w(e)
                    dispatcher.notifyFailed(task)
                }

            }
        }
    }

    @Throws(Exception::class)
    private fun runTask(task: DownloadTask) {
        val sourceUrl = task.sourceUrl
        val sourceFile = downloadPage(sourceUrl)

        val destinationUrl = task.destinationUrl
        val destinationFile = File(destinationUrl)

        FileUtils.copyFile(sourceFile, destinationFile)
    }

    @Throws(Exception::class)
    private fun downloadPage(pageUrl: String): File {
        return Glide.with(this)
            .download(pageUrl)
            .submit()
            .get()
    }
}

private fun Context.progressNotification(): Notification =
    NotificationCompat.Builder(this, Constants.DEFAULT_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification_download)
        .setContentTitle("Downloading")
        .setProgress(0, 0, true)
        .build()
