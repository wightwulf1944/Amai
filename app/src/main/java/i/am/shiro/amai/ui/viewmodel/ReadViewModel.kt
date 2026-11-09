package i.am.shiro.amai.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.am.shiro.amai.data.repository.ReadRepository
import i.am.shiro.amai.model.Page
import kotlinx.coroutines.launch

class ReadViewModel(
    private val repository: ReadRepository,
    private val bookId: Int,
) : ViewModel() {

    var pages by mutableStateOf(emptyList<Page>())
        private set

    init {
        viewModelScope.launch {
            pages = repository.getPages(bookId)
        }
    }
}
