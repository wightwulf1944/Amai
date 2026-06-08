package i.am.shiro.amai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _searchEvent = MutableSharedFlow<SearchEvent>(replay = 1)
    val searchEvent = _searchEvent.asSharedFlow()

    fun search(query: String) {
        viewModelScope.launch {
            _searchEvent.emit(SearchEvent(query))
        }
    }
}

class SearchEvent(val query: String) {
    var isHomeConsumed = false
    var isNhentaiConsumed = false
}
