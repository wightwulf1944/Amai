package i.am.shiro.amai.util

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T> SavedStateHandle.savedMutableStateFlow(default: T) =
    object : ReadOnlyProperty<Any, MutableStateFlow<T>> {

        private var flow: MutableStateFlow<T>? = null

        override fun getValue(thisRef: Any, property: KProperty<*>): MutableStateFlow<T> {
            return flow ?: getMutableStateFlow(property.name, default).also { flow = it }
        }
    }
