package i.am.shiro.amai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val searchEventLive = MutableLiveData<SearchEvent>()

    fun search(query: String) {
        searchEventLive.value = SearchEvent(query)
    }
}

class SearchEvent(val query: String) {
    var isHomeConsumed = false
    var isNhentaiConsumed = false
}
