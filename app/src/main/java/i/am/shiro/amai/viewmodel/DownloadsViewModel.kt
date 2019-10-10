package i.am.shiro.amai.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.model.DownloadJob
import io.realm.Realm

class DownloadsViewModel : ViewModel() {

    private val realm = Realm.getDefaultInstance()

    private val _downloadJobsLive = MutableLiveData<List<DownloadJob>>()

    val downloadJobsLive: LiveData<List<DownloadJob>>
        get() = _downloadJobsLive


    init {
        val downloadJobs = realm.where(DownloadJob::class.java)
                .findAll()
                .createSnapshot()

        _downloadJobsLive.value = downloadJobs
    }

    fun dismissJob(job: DownloadJob) {
        realm.beginTransaction()
        job.deleteFromRealm()
        realm.commitTransaction()
    }

    fun retryJob(job: DownloadJob) {

    }

    fun cancelJob(job: DownloadJob) {

    }

    fun pauseJob(it: DownloadJob) {

    }

    override fun onCleared() {
        super.onCleared()
        realm.close()
    }
}
