package i.am.shiro.amai.util

import android.os.Bundle
import androidx.fragment.app.Fragment

fun Fragment.buildArguments(bundleBlock: Bundle.() -> Unit) {
    val bundle = Bundle()
    bundle.bundleBlock()
    arguments = bundle
}