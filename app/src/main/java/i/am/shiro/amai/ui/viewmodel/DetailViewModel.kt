package i.am.shiro.amai.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.am.shiro.amai.data.repository.BookRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModel(
    private val bookId: Int,
    private val repository: BookRepository
) : ViewModel() {

    val uiState = repository.getBookDetail(bookId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            try {
                repository.refreshBookDetail(bookId)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun onFavoriteToggle(isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                repository.toggleFavorite(bookId, isFavorite)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}
