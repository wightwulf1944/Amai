package i.am.shiro.amai.util

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import kotlin.reflect.KMutableProperty0

fun <T> Bundle.put(key: String, value: T) {
    when (value) {
        is Boolean -> putBoolean(key, value)
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Short -> putShort(key, value)
        is Long -> putLong(key, value)
        is Byte -> putByte(key, value)
        is ByteArray -> putByteArray(key, value)
        is Char -> putChar(key, value)
        is CharArray -> putCharArray(key, value)
        is CharSequence -> putCharSequence(key, value)
        is Float -> putFloat(key, value)
        is Bundle -> putBundle(key, value)
        is Parcelable -> putParcelable(key, value)
        is Serializable -> putSerializable(key, value)
        else -> throw IllegalStateException("Type of property $key is not supported")
    }
}

fun Bundle.loadBoolean(property: KMutableProperty0<Boolean>) {
    property.set(getBoolean(property.name))
}

fun Bundle.saveBoolean(property: KMutableProperty0<Boolean>) {
    putBoolean(property.name, property.get())
}

fun Bundle.loadInt(property: KMutableProperty0<Int>) {
    property.set(getInt(property.name))
}

fun Bundle.saveInt(property: KMutableProperty0<Int>) {
    putInt(property.name, property.get())
}
