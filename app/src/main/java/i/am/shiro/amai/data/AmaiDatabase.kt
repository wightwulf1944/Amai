package i.am.shiro.amai.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import i.am.shiro.amai.data.dao.BookDao
import i.am.shiro.amai.data.dao.CachedDao
import i.am.shiro.amai.data.dao.CachedPreviewDao
import i.am.shiro.amai.data.dao.DetailDao
import i.am.shiro.amai.data.dao.FavoriteDao
import i.am.shiro.amai.data.dao.FavoritesPreviewDao
import i.am.shiro.amai.data.dao.ImageDao
import i.am.shiro.amai.data.dao.TagDao
import i.am.shiro.amai.data.entity.BookEntity
import i.am.shiro.amai.data.entity.CachedEntity
import i.am.shiro.amai.data.entity.FavoriteEntity
import i.am.shiro.amai.data.entity.ImageEntity
import i.am.shiro.amai.data.entity.TagEntity
import i.am.shiro.amai.data.view.CachedPreviewView
import i.am.shiro.amai.data.view.FavoritesPreviewView

@Database(
    version = 29,
    exportSchema = true,
    entities = [
        BookEntity::class,
        TagEntity::class,
        ImageEntity::class,
        FavoriteEntity::class,
        CachedEntity::class
    ],
    views = [
        FavoritesPreviewView::class,
        CachedPreviewView::class,
    ],
    autoMigrations = [
        AutoMigration(from = 27, to = 28, spec = AmaiDatabase.Migration27To28::class),
        AutoMigration(from = 28, to = 29, spec = AmaiDatabase.Migration28To29::class)
    ]
)
abstract class AmaiDatabase : RoomDatabase() {

    abstract val bookDao: BookDao

    abstract val tagDao: TagDao

    abstract val imageDao: ImageDao

    abstract val favoriteDao: FavoriteDao

    abstract val cachedDao: CachedDao

    abstract val favoritesPreviewDao: FavoritesPreviewDao

    abstract val cachedPreviewDao: CachedPreviewDao

    abstract val detailDao: DetailDao

    @RenameColumn(
        tableName = "FavoriteEntity",
        fromColumnName = "saveDate",
        toColumnName = "favoriteDate"
    )
    class Migration27To28 : AutoMigrationSpec

    @RenameColumn(
        tableName = "BookEntity",
        fromColumnName = "thumbnailUrl",
        toColumnName = "thumbnailPath"
    )
    @RenameColumn(
        tableName = "ImageEntity",
        fromColumnName = "thumbnailUrl",
        toColumnName = "thumbnailPath"
    )
    @RenameColumn(
        tableName = "ImageEntity",
        fromColumnName = "url",
        toColumnName = "path"
    )
    class Migration28To29 : AutoMigrationSpec
}
