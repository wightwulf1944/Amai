package i.am.shiro.amai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.DATABASE
import i.am.shiro.amai.SavedSort
import i.am.shiro.amai.data.view.SavedPreviewView
import i.am.shiro.amai.util.delegate
import io.reactivex.disposables.Disposables

class SavedViewModel(handle: SavedStateHandle) : ViewModel() {

    private var disposable = Disposables.disposed()

    private var query by handle.delegate("%")

    private var sort by handle.delegate(SavedSort.New)

    val booksLive = MutableLiveData<List<SavedPreviewView>>()

    init {
        fetchLocal()
    }

    fun onSearch(query: String) {
        this.query = "%$query%"
        fetchLocal()
    }

    fun onSort(sort: SavedSort) {
        this.sort = sort
        fetchLocal()
    }

    private fun fetchLocal() {
        disposable.dispose()
        disposable = DATABASE.savedPreviewDao
            .findSorted(query, sort)
            .subscribe(booksLive::postValue)
    }

    override fun onCleared() {
        disposable.dispose()
    }
}
