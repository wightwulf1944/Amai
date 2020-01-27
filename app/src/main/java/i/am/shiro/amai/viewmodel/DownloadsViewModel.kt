package i.am.shiro.amai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.DATABASE
import i.am.shiro.amai.DownloadStatus
import i.am.shiro.amai.data.model.Download
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers.io

class DownloadsViewModel : ViewModel() {

    private var disposable = Disposables.disposed()

    val downloadJobsLive = MutableLiveData<List<Download>>()

    init {
        disposable = DATABASE.downloadDetailDao.getAll()
            .subscribeOn(io())
            .map { list ->
                list.map { item ->
                    Download(
                        bookId = item.bookId,
                        title = item.title,
                        progress = item.progress,
                        progressMax = item.progressMax,
                        status = DownloadStatus.QUEUED // TODO status should be derived from other factors
                    )
                }
            }
            .subscribe(downloadJobsLive::postValue)
    }

    override fun onCleared() = disposable.dispose()

    fun dismiss(download: Download) {
        DATABASE.downloadDao.delete(download.bookId).subscribe()
    }

    fun retry(download: Download) {
        DATABASE.downloadDao.resetErrorCount(download.bookId).subscribe()
    }

    fun cancel(download: Download) {
        // TODO also delete downloaded files here
        dismiss(download)
    }

    fun pause(download: Download) {
        DATABASE.downloadDao.setPaused(download.bookId, true).subscribe()
    }

    fun resume(download: Download) {
        DATABASE.downloadDao.setPaused(download.bookId, false).subscribe()
    }

    fun pauseAll() {
        DATABASE.downloadDao.setAllPaused(true).subscribe()
    }

    fun resumeAll() {
        DATABASE.downloadDao.setAllPaused(false).subscribe()
    }
}
