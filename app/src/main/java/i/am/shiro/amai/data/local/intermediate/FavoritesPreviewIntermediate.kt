package i.am.shiro.amai.data.local.intermediate

import androidx.room.Embedded
import androidx.room.Relation
import i.am.shiro.amai.data.local.entity.BookEntity
import i.am.shiro.amai.data.local.entity.FavoriteEntity

class FavoritesPreviewIntermediate(
    @Embedded
    val favorite: FavoriteEntity,

    @Relation(
        parentColumn = "bookId",
        entityColumn = "bookId"
    )
    val book: BookEntity
)
