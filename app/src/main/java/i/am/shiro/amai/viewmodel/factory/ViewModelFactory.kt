package i.am.shiro.amai.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import i.am.shiro.amai.AmaiPreferences
import i.am.shiro.amai.dagger.AmaiComponent
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.viewmodel.DetailViewModel
import i.am.shiro.amai.viewmodel.DownloadsViewModel
import i.am.shiro.amai.viewmodel.LoadingViewModel
import i.am.shiro.amai.viewmodel.ReadViewModel

class ViewModelFactory(
    private val component: AmaiComponent
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        when (modelClass) {
            DetailViewModel::class.java -> DetailViewModel(component.database)
            LoadingViewModel::class.java -> LoadingViewModel(component.database)
            ReadViewModel::class.java -> ReadViewModel(component.database)
            DownloadsViewModel::class.java -> DownloadsViewModel(component.database)
            else -> error("Unable to create $modelClass")
        } as T
}