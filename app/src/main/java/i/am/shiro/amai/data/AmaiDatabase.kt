package i.am.shiro.amai.data

import androidx.room.Database
import androidx.room.RoomDatabase
import i.am.shiro.amai.data.dao.*
import i.am.shiro.amai.data.entity.*
import i.am.shiro.amai.data.view.*

@Database(
    entities = [
        BookEntity::class,
        TagEntity::class,
        DownloadJobEntity::class,
        LocalImageEntity::class,
        RemoteImageEntity::class,
        SavedEntity::class,
        CachedEntity::class
    ],
    views = [
        SavedPreviewView::class,
        CachedPreviewView::class,
        ThumbnailView::class,
        PageView::class
    ],
    version = 23,
    exportSchema = false)
abstract class AmaiDatabase : RoomDatabase() {

    abstract val multiTableDao: MultiTableDao

    abstract val bookDao: BookDao

    abstract val tagDao: TagDao

    abstract val downloadDao: DownloadDao

    abstract val localImageDao: LocalImageDao

    abstract val remoteImageDao: RemoteImageDao

    abstract val cachedDao: CachedDao

    abstract val savedDao: SavedDao

    abstract val savedPreviewDao: SavedPreviewDao

    abstract val cachedPreviewDao: CachedPreviewDao

    abstract val thumbnailDao: ThumbnailDao

    abstract val pageDao: PageDao
}
