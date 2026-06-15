package i.am.shiro.amai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.entity.ImageEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ReadViewModel(private val database: AmaiDatabase) : ViewModel() {

    private var currentBookId: Int? = null

    val pages = MutableStateFlow<List<ImageEntity>>(emptyList())

    fun setBookId(bookId: Int) {
        if (currentBookId == bookId) return
        currentBookId = bookId

        viewModelScope.launch {
            pages.value = database.imageDao.findByBookId(bookId)
        }
    }
}
