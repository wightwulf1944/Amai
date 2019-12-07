package i.am.shiro.amai.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <reified T> Fragment.argument() = object : ReadWriteProperty<Any, T> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T =
        requireArguments().get(property.name) as T

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        val arguments = arguments ?: Bundle().also(::setArguments)
        arguments.put(property.name, value)
    }
}