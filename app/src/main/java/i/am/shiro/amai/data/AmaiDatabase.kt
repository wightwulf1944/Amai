package i.am.shiro.amai.data

import androidx.room.Database
import androidx.room.RoomDatabase
import i.am.shiro.amai.data.dao.BookDao
import i.am.shiro.amai.data.dao.CachedDao
import i.am.shiro.amai.data.dao.CachedPreviewDao
import i.am.shiro.amai.data.dao.DetailDao
import i.am.shiro.amai.data.dao.FavoriteDao
import i.am.shiro.amai.data.dao.ImageDao
import i.am.shiro.amai.data.dao.SavedPreviewDao
import i.am.shiro.amai.data.dao.TagDao
import i.am.shiro.amai.data.entity.BookEntity
import i.am.shiro.amai.data.entity.CachedEntity
import i.am.shiro.amai.data.entity.FavoriteEntity
import i.am.shiro.amai.data.entity.ImageEntity
import i.am.shiro.amai.data.entity.TagEntity
import i.am.shiro.amai.data.view.CachedPreviewView
import i.am.shiro.amai.data.view.SavedPreviewView

@Database(
    version = 27,
    exportSchema = true,
    entities = [
        BookEntity::class,
        TagEntity::class,
        ImageEntity::class,
        FavoriteEntity::class,
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

    abstract val imageDao: ImageDao

    abstract val favoriteDao: FavoriteDao

    abstract val cachedDao: CachedDao

    abstract val savedPreviewDao: SavedPreviewDao

    abstract val cachedPreviewDao: CachedPreviewDao

    abstract val detailDao: DetailDao
}
