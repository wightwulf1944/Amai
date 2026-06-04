package i.am.shiro.amai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.FavoritesSort
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.view.FavoritesPreviewView
import i.am.shiro.amai.util.invoke
import io.reactivex.rxjava3.disposables.Disposable

class FavoritesViewModel(
    handle: SavedStateHandle,
    private val database: AmaiDatabase
) : ViewModel() {

    private var disposable = Disposable.disposed()

    private var query by handle<String>("%")

    private var sort by handle<FavoritesSort>(FavoritesSort.New)

    val booksLive = MutableLiveData<List<FavoritesPreviewView>>()

    init {
        fetchLocal()
    }

    fun onSearch(query: String) {
        this.query = "%$query%"
        fetchLocal()
    }

    fun onSort(sort: FavoritesSort) {
        this.sort = sort
        fetchLocal()
    }

    private fun fetchLocal() {
        disposable.dispose()
        disposable = database.favoritesPreviewDao
            .findSorted(query, sort)
            .subscribe(booksLive::postValue)
    }

    override fun onCleared() {
        disposable.dispose()
    }
}
