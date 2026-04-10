package i.am.shiro.amai.data.intermediate

import androidx.room.Embedded
import androidx.room.Relation
import i.am.shiro.amai.data.entity.BookEntity
import i.am.shiro.amai.data.entity.RemoteImageEntity
import i.am.shiro.amai.data.entity.TagEntity

data class DetailIntermediate(
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
    val remoteImageEntities: List<RemoteImageEntity>
)