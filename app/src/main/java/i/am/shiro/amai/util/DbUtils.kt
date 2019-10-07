package i.am.shiro.amai.util

import i.am.shiro.amai.constant.DownloadStatus
import i.am.shiro.amai.model.DownloadJob
import io.realm.Realm


/**
 * Utility function that can be used for development purposes while a dismiss done download is not yet implemented.
 */
fun clearDoneDownloads() {
    val realm = Realm.getDefaultInstance()
    realm.executeTransaction {
        it.where(DownloadJob::class.java)
                .equalTo("status", DownloadStatus.DONE)
                .findAll()
                .deleteAllFromRealm()
    }
    realm.close()
}