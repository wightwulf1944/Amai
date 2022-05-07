package i.am.shiro.amai.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import i.am.shiro.amai.AmaiPreferences
import i.am.shiro.amai.DEFAULT_CHANNEL_ID
import i.am.shiro.amai.R
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.entity.LocalImageEntity
import i.am.shiro.amai.data.entity.SavedEntity
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors

private const val ID_PROGRESS = 1
private const val ID_DONE = 2
private const val ID_ERROR = 3

class DownloadService : Service() {

    private val database by inject<AmaiDatabase>()

    private val preferences by inject<AmaiPreferences>()

    private val okHttpClient by inject<OkHttpClient>()

    private val notifManager by lazy { getSystemService<NotificationManager>()!! }

    private val executor = Executors.newSingleThreadExecutor()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    override fun onCreate() {
        super.onCreate()
        startForeground(ID_PROGRESS, buildForegroundNotification())
        executor.execute(::work)
    }

    // TODO resize downloaded image as thumbnail and save it
    private fun work() {
        val download = database.downloadDao.findAvailableJob() ?: return stopSelf()
        val book = database.bookDao.findById(download.bookId)
        val remoteImage = database.remoteImageDao.findByBookIdAndPageIndex(
            download.bookId,
            download.progressIndex
        )

        database.downloadDao.setIsDownloading(download.bookId, true)

        postDownloadProgressNotification(book.title, book.pageCount, download.progressIndex)

        val localFile = File(preferences.storagePath!!)
            .resolve(remoteImage.bookId.toString())
            .apply { mkdirs() }
            .resolve(remoteImage.url.substringAfterLast('/'))

        try {
            downloadPageTo(remoteImage.url, localFile)
            val localImage = LocalImageEntity(
                bookId = download.bookId,
                pageIndex = download.progressIndex,
                width = remoteImage.width,
                height = remoteImage.height,
                url = localFile.path,
                thumbnailWidth = remoteImage.thumbnailWidth,
                thumbnailHeight = remoteImage.thumbnailHeight,
                thumbnailUrl = remoteImage.thumbnailUrl
            )
            database.localImageDao.insert(localImage)
            database.downloadDao.incrementProgress(download.bookId)

            if (download.progressIndex == book.pageCount - 1) {
                database.savedDao.insert(SavedEntity(download.bookId))
                database.downloadDao.setIsDone(download.bookId, true)
                postDownloadDoneNotification(book.title)
            }
        } catch (e: Exception) {
            Timber.e(e)
            database.downloadDao.incrementErrorCount(download.bookId)

            if (download.errorCount + 1 == 3) {
                postDownloadFailedNotification(book.title)
            }
        }

        val nextJob = database.downloadDao.findAvailableJob()
        if (nextJob?.bookId != download.bookId) {
            database.downloadDao.setIsDownloading(download.bookId, false)
        }

        executor.execute(::work)
    }

    @Throws(Exception::class)
    private fun downloadPageTo(pageUrl: String, toFile: File) {
        val request = Request.Builder()
            .url(pageUrl)
            .build()

        okHttpClient.newCall(request).execute().use {
            if (!it.isSuccessful) throw IOException("Unexpected code $it")
            toFile.delete()
            toFile.sink().buffer().writeAll(it.body!!.source())
        }
    }

    private fun buildForegroundNotification(): Notification =
        NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_download)
            .setContentTitle("Downloading")
            .setProgress(0, 0, true)
            .build()

    private fun postDownloadProgressNotification(title: String, max: Int, progress: Int) {
        NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_download)
            .setContentTitle("Downloading")
            .setContentText(title)
            .setProgress(max, progress, false)
            .post(ID_PROGRESS)
    }

    private fun postDownloadDoneNotification(title: String) {
        NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_download)
            .setContentTitle("Download done")
            .setContentText(title)
            .post(ID_DONE)
    }

    private fun postDownloadFailedNotification(title: String) {
        NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_download)
            .setContentTitle("Download failed")
            .setContentText(title)
            .post(ID_ERROR)
    }

    private fun NotificationCompat.Builder.post(id: Int) = notifManager.notify(id, build())
}

