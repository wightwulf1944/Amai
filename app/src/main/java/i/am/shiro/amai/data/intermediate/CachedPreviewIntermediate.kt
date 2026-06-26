package i.am.shiro.amai.data.intermediate

import androidx.room.Embedded
import androidx.room.Relation
import i.am.shiro.amai.data.entity.BookEntity
import i.am.shiro.amai.data.entity.CachedEntity
import i.am.shiro.amai.data.entity.FavoriteEntity

data class CachedPreviewIntermediate(
    @Embedded
    val cached: CachedEntity,

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
