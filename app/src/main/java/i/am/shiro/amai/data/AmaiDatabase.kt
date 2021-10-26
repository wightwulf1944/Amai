package i.am.shiro.amai.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import i.am.shiro.amai.data.dao.*
import i.am.shiro.amai.data.entity.*
import i.am.shiro.amai.data.view.CachedPreviewView
import i.am.shiro.amai.data.view.PageView
import i.am.shiro.amai.data.view.SavedPreviewView
import i.am.shiro.amai.data.view.ThumbnailView

@Database(
    version = 24,
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
    ],
    autoMigrations = [
        AutoMigration(from = 23, to = 24, spec = AmaiDatabase.Migration23to24::class)
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

    @DeleteColumn(tableName = "BookEntity", columnName = "webUrl")
    class Migration23to24 : AutoMigrationSpec
}
