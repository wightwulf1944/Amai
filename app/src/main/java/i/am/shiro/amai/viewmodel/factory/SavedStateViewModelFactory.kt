package i.am.shiro.amai.viewmodel.factory

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import i.am.shiro.amai.viewmodel.NhentaiViewModel
import i.am.shiro.amai.viewmodel.SavedViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class SavedStateViewModelFactory(
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null), KoinComponent {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle) =
        when (modelClass) {
            NhentaiViewModel::class.java -> NhentaiViewModel(handle, get(), get())
            SavedViewModel::class.java -> SavedViewModel(handle, get())
            else -> error("Unable to create $modelClass")
        } as T
}