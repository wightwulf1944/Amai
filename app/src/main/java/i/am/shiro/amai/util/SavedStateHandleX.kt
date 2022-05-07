package i.am.shiro.amai.util

import androidx.lifecycle.SavedStateHandle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

operator fun <T> SavedStateHandle.invoke(default: T) = object : ReadWriteProperty<Any, T> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T =
        get(property.name) ?: default

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        set(property.name, value)
    }
}
