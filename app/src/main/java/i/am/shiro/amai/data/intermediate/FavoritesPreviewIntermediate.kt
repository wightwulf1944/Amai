package i.am.shiro.amai.data.intermediate

import androidx.room.Embedded
import androidx.room.Relation
import i.am.shiro.amai.data.entity.BookEntity
import i.am.shiro.amai.data.entity.FavoriteEntity

data class FavoritesPreviewIntermediate(
    @Embedded
    val favorite: FavoriteEntity,

    @Relation(
        parentColumn = "bookId",
        entityColumn = "bookId"
    )
    val book: BookEntity
)
