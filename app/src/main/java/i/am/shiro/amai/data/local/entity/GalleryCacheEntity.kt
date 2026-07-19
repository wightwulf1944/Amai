package i.am.shiro.amai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.Uuid

@Entity
data class GalleryCacheEntity(
    @PrimaryKey val id: Uuid,
    val nextPage: Int?
)
