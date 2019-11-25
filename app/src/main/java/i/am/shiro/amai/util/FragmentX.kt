package i.am.shiro.amai.util

import android.os.Bundle
import androidx.fragment.app.Fragment

fun Fragment.buildArguments(bundleBlock: Bundle.() -> Unit) {
    arguments = Bundle().apply(bundleBlock)
}