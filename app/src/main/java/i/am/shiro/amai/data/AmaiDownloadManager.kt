package i.am.shiro.amai.data

import androidx.lifecycle.MutableLiveData
import i.am.shiro.amai.DownloadStatus
import i.am.shiro.amai.data.model.Download
import i.am.shiro.amai.data.model.DownloadDetail
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.schedulers.Schedulers.io
import timber.log.Timber

class AmaiDownloadManager(private val db: AmaiDatabase) {

    private val disposable = CompositeDisposable()

    private var currentDownloadBookId: Int = 0

    val downloadsLive = MutableLiveData<List<Download>>()

    init {
        disposable += db.multiTableDao.getDownloadDetails()
            .subscribeOn(io())
            .subscribe(::onUpdate, Timber::e)
    }

    private fun onUpdate(downloadDetails: List<DownloadDetail>) {
        val downloads = downloadDetails.map {
            Download(
                bookId = it.bookId,
                title = it.title,
                progress = it.localImageCount,
                progressMax = it.pageCount,
                status = when {
                    it.errorCount >= 3 -> DownloadStatus.FAILED
                    it.localImageCount == it.pageCount -> DownloadStatus.DONE
                    it.isPaused -> DownloadStatus.PAUSED
                    currentDownloadBookId == it.bookId -> DownloadStatus.RUNNING
                    else -> DownloadStatus.QUEUED
                }
            )
        }

        downloadsLive.postValue(downloads)
    }

    fun dispose() = disposable.dispose()

    fun dismiss(download: Download) {
        disposable += db.downloadDao.delete(download.bookId)
            .subscribeOn(io())
            .subscribe()
    }

    fun retry(download: Download) {
        disposable += db.downloadDao.resetErrorCount(download.bookId)
            .subscribeOn(io())
            .subscribe()
    }

    fun cancel(download: Download) {
        // TODO also delete downloaded files here
        dismiss(download)
    }

    fun pause(download: Download) {
        disposable += db.downloadDao.setPaused(download.bookId, true)
            .subscribeOn(io())
            .subscribe()
    }

    fun resume(download: Download) {
        disposable += db.downloadDao.setPaused(download.bookId, false)
            .subscribeOn(io())
            .subscribe()
    }

    fun pauseAll() {
        disposable += db.downloadDao.setAllPaused(true)
            .subscribeOn(io())
            .subscribe()
    }

    fun resumeAll() {
        disposable += db.downloadDao.setAllPaused(false)
            .subscribeOn(io())
            .subscribe()
    }
}