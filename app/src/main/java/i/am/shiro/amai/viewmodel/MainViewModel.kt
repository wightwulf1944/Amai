package i.am.shiro.amai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val navigationPathLive = MutableLiveData<NavigationPath>()
}

class NavigationPath(val payload: String, vararg pathSegments: PathSegment) {

    var segments = pathSegments.asList()

    fun traverse(current: PathSegment, action: (PathSegment?) -> Unit) {
        if (segments.firstOrNull() == current) {
            segments = segments.drop(1)
            action(segments.firstOrNull())
        }
    }
}


interface PathSegment
object Home : PathSegment
object Nhentai : PathSegment
object Search : PathSegment