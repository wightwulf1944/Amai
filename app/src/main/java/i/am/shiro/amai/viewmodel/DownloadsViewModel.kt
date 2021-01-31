package i.am.shiro.amai.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.AmaiDownloadManager
import i.am.shiro.amai.data.model.Download

// TODO anemic ViewModel might not be needed
class DownloadsViewModel(database: AmaiDatabase) : ViewModel() {

    private var downloadManager = AmaiDownloadManager(database)

    val downloadsLive: LiveData<List<Download>> = downloadManager.downloadsLive

    override fun onCleared() = downloadManager.dispose()

    fun dismiss(download: Download) = downloadManager.dismiss(download)

    fun retry(download: Download) = downloadManager.retry(download)

    fun cancel(download: Download) = downloadManager.cancel(download)

    fun pause(download: Download) = downloadManager.pause(download)

    fun resume(download: Download) = downloadManager.resume(download)

    fun pauseAll() = downloadManager.pauseAll()

    fun resumeAll() = downloadManager.resumeAll()
}
