package i.am.shiro.amai.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.entity.ImageEntity
import kotlinx.coroutines.launch

class ReadViewModel(
    private val bookId: Int,
    private val database: AmaiDatabase
) : ViewModel() {

    var pages by mutableStateOf(emptyList<ImageEntity>())
        private set

    init {
        viewModelScope.launch {
            pages = database.imageDao.findByBookId(bookId)
        }
    }
}
