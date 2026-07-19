package i.am.shiro.amai.data.local

import androidx.room.TypeConverter
import kotlin.uuid.Uuid

class Converters {
    @TypeConverter
    fun fromUuid(uuid: Uuid) = uuid.toString()

    @TypeConverter
    fun toUuid(uuid: String) = Uuid.parse(uuid)
}
