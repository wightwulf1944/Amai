package i.am.shiro.amai.data.local.intermediate

import androidx.room.Embedded
import androidx.room.Relation
import i.am.shiro.amai.data.local.entity.BookEntity
import i.am.shiro.amai.data.local.entity.FavoriteEntity
import i.am.shiro.amai.data.local.entity.GalleryCacheEntryEntity

class CachedPreviewIntermediate(
    @Embedded
    val entry: GalleryCacheEntryEntity,

    @Relation(
        parentColumn = "bookId",
        entityColumn = "bookId"
    )
    val book: BookEntity,

    @Relation(
        parentColumn = "bookId",
        entityColumn = "bookId"
    )
    val favorite: FavoriteEntity?
)
