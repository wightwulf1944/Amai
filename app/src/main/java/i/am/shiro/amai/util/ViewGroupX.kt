package i.am.shiro.amai.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.util.Consumer
import androidx.core.view.forEach

fun ViewGroup.inflateChild(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

inline fun <reified T : View> ViewGroup.addChild(@LayoutRes layoutRes: Int, block: T.() -> Unit) {
    val child = inflateChild(layoutRes) as T
    addView(child.apply(block))
}

fun ViewGroup.forEach(childConsumer: Consumer<View>) = forEach(childConsumer::accept)