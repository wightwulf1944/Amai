package i.am.shiro.amai.data

import androidx.room.Database
import androidx.room.RoomDatabase
import i.am.shiro.amai.data.dao.BookDao
import i.am.shiro.amai.data.dao.CachedDao
import i.am.shiro.amai.data.dao.CachedPreviewDao
import i.am.shiro.amai.data.dao.DetailDao
import i.am.shiro.amai.data.dao.ImageDao
import i.am.shiro.amai.data.dao.SavedDao
import i.am.shiro.amai.data.dao.SavedPreviewDao
import i.am.shiro.amai.data.dao.TagDao
import i.am.shiro.amai.data.entity.BookEntity
import i.am.shiro.amai.data.entity.CachedEntity
import i.am.shiro.amai.data.entity.ImageEntity
import i.am.shiro.amai.data.entity.SavedEntity
import i.am.shiro.amai.data.entity.TagEntity
import i.am.shiro.amai.data.view.CachedPreviewView
import i.am.shiro.amai.data.view.SavedPreviewView

@Database(
    version = 26,
    exportSchema = true,
    entities = [
        BookEntity::class,
        TagEntity::class,
        ImageEntity::class,
        SavedEntity::class,
        CachedEntity::class
    ],
    views = [
        SavedPreviewView::class,
        CachedPreviewView::class,
    ]
)
abstract class AmaiDatabase : RoomDatabase() {

    abstract val bookDao: BookDao

    abstract val tagDao: TagDao

    abstract val remoteImageDao: ImageDao

    abstract val savedDao: SavedDao

    abstract val cachedDao: CachedDao

    abstract val savedPreviewDao: SavedPreviewDao

    abstract val cachedPreviewDao: CachedPreviewDao

    abstract val detailDao: DetailDao
}
