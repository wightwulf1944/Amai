package i.am.shiro.amai.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.am.shiro.amai.data.entity.ImageEntity
import i.am.shiro.amai.repository.ReadRepository
import kotlinx.coroutines.launch

class ReadViewModel(
    private val bookId: Int,
    private val repository: ReadRepository
) : ViewModel() {

    var pages by mutableStateOf(emptyList<ImageEntity>())
        private set

    init {
        viewModelScope.launch {
            pages = repository.getPages(bookId)
        }
    }
}
