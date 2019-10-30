package i.am.shiro.amai.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.model.DownloadJob
import io.realm.Realm
import io.realm.kotlin.where

class DownloadsViewModel : ViewModel() {

    private val realm = Realm.getDefaultInstance()

    private val _downloadJobsLive = MutableLiveData<List<DownloadJob>>()

    private val disposable = realm.where<DownloadJob>()
        .findAllAsync()
        .asFlowable()
        .filter { it.isLoaded }
        .map { realm.copyFromRealm(it) }
        .subscribe { _downloadJobsLive.postValue(it) }

    val downloadJobsLive: LiveData<List<DownloadJob>>
        get() = _downloadJobsLive

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
        realm.close()
    }

    fun dismissJob(job: DownloadJob) {
        realm.beginTransaction()
        realm.where<DownloadJob>()
            .equalTo("bookId", job.bookId)
            .findFirst()
            ?.deleteFromRealm()
        realm.commitTransaction()
    }

    fun retryJob(job: DownloadJob) {

    }

    fun cancelJob(job: DownloadJob) {
        // TODO also delete downloaded files here
        dismissJob(job)
    }

    fun pauseJob(it: DownloadJob) {

    }
}
