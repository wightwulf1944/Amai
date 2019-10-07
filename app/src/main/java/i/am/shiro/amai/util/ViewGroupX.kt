package i.am.shiro.amai.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun <T : View> ViewGroup.inflateChild(@LayoutRes layoutRes: Int): T {
    val childView = LayoutInflater.from(context).inflate(layoutRes, this, false)
    addView(childView)
    return childView as T
}