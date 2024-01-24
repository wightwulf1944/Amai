package i.am.shiro.amai.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import i.am.shiro.amai.viewmodel.DetailViewModel
import i.am.shiro.amai.viewmodel.DownloadsViewModel
import i.am.shiro.amai.viewmodel.LoadingViewModel
import i.am.shiro.amai.viewmodel.ReadViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class ViewModelFactory : ViewModelProvider.Factory, KoinComponent {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        when (modelClass) {
            DetailViewModel::class.java -> DetailViewModel(get())
            LoadingViewModel::class.java -> LoadingViewModel(get(), get())
            ReadViewModel::class.java -> ReadViewModel(get())
            DownloadsViewModel::class.java -> DownloadsViewModel(get())
            else -> error("Unable to create $modelClass")
        } as T
}