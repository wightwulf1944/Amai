package i.am.shiro.amai.viewmodel.factory

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import i.am.shiro.amai.dagger.AmaiComponent
import i.am.shiro.amai.viewmodel.NhentaiViewModel
import i.am.shiro.amai.viewmodel.SavedViewModel

class SavedStateViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val component: AmaiComponent
) : AbstractSavedStateViewModelFactory(owner, null) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle) =
        when (modelClass) {
            NhentaiViewModel::class.java -> NhentaiViewModel(handle, component.database)
            SavedViewModel::class.java -> SavedViewModel(handle, component.database)
            else -> error("Unable to create $modelClass")
        } as T
}