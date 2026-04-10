package i.am.shiro.amai.data

import androidx.room.Database
import androidx.room.RoomDatabase
import i.am.shiro.amai.data.dao.BookDao
import i.am.shiro.amai.data.dao.CachedDao
import i.am.shiro.amai.data.dao.CachedPreviewDao
import i.am.shiro.amai.data.dao.DetailDao
import i.am.shiro.amai.data.dao.DownloadDao
import i.am.shiro.amai.data.dao.LocalImageDao
import i.am.shiro.amai.data.dao.MultiTableDao
import i.am.shiro.amai.data.dao.PageDao
import i.am.shiro.amai.data.dao.RemoteImageDao
import i.am.shiro.amai.data.dao.SavedDao
import i.am.shiro.amai.data.dao.SavedPreviewDao
import i.am.shiro.amai.data.dao.TagDao
import i.am.shiro.amai.data.dao.ThumbnailDao
import i.am.shiro.amai.data.entity.BookEntity
import i.am.shiro.amai.data.entity.CachedEntity
import i.am.shiro.amai.data.entity.DownloadJobEntity
import i.am.shiro.amai.data.entity.LocalImageEntity
import i.am.shiro.amai.data.entity.RemoteImageEntity
import i.am.shiro.amai.data.entity.SavedEntity
import i.am.shiro.amai.data.entity.TagEntity
import i.am.shiro.amai.data.view.CachedPreviewView
import i.am.shiro.amai.data.view.PageView
import i.am.shiro.amai.data.view.SavedPreviewView
import i.am.shiro.amai.data.view.ThumbnailView

@Database(
    version = 25,
    exportSchema = true,
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
    ]
)
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

    abstract val detailDao: DetailDao
}
