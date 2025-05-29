package i.am.shiro.amai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val searchEventLive = MutableLiveData<SearchEvent>()

    class SearchEvent(private val search: String) {
        var isConsumed = false

        fun consume(action: (String) -> Unit) {
            if (isConsumed) return
            action(search)
            isConsumed = true
        }
    }
}