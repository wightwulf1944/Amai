package i.am.shiro.amai.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Consumer
import androidx.core.view.forEach
import androidx.viewbinding.ViewBinding

fun <T : ViewBinding> ViewGroup.inflateChild(binder: (LayoutInflater, ViewGroup, Boolean) -> T): T {
    val inflater = LayoutInflater.from(context)
    return binder(inflater, this, false)
}

fun <T : ViewBinding> ViewGroup.addChild(binder: (LayoutInflater, ViewGroup, Boolean) -> T, block: T.() -> Unit) {
    val inflater = LayoutInflater.from(context)
    binder(inflater, this, true).block()
}

fun ViewGroup.forEach(childConsumer: Consumer<View>) = forEach(childConsumer::accept)