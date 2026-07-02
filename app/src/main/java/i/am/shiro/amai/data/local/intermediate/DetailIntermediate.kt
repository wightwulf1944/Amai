package i.am.shiro.amai.data.local.intermediate

import androidx.room.Embedded
import androidx.room.Relation
import i.am.shiro.amai.data.local.entity.BookEntity
import i.am.shiro.amai.data.local.entity.FavoriteEntity
import i.am.shiro.amai.data.local.entity.ImageEntity
import i.am.shiro.amai.data.local.entity.TagEntity

class DetailIntermediate(
    @Embedded val bookEntity: BookEntity,
    @Relation(
        parentColumn = "bookId",
        entityColumn = "bookId"
    )
    val tagEntities: List<TagEntity>,
    @Relation(
        parentColumn = "bookId",
        entityColumn = "bookId"
    )
    val remoteImageEntities: List<ImageEntity>,
    @Relation(
        parentColumn = "bookId",
        entityColumn = "bookId"
    )
    val favoriteEntity: FavoriteEntity?
)