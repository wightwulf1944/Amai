package i.am.shiro.amai.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import i.am.shiro.amai.viewmodel.DetailViewModel
import i.am.shiro.amai.viewmodel.NhentaiViewModel
import i.am.shiro.amai.viewmodel.ReadViewModel
import i.am.shiro.amai.viewmodel.SavedViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import kotlin.reflect.KClass

class ViewModelFactory : ViewModelProvider.Factory, KoinComponent {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T =
        when (modelClass) {
            ReadViewModel::class ->
                ReadViewModel(get()) as T

            DetailViewModel::class ->
                DetailViewModel(extras.createSavedStateHandle(), get(), get()) as T

            NhentaiViewModel::class ->
                NhentaiViewModel(extras.createSavedStateHandle(), get(), get()) as T

            SavedViewModel::class ->
                SavedViewModel(extras.createSavedStateHandle(), get()) as T

            else ->
                error("Unable to create $modelClass")
        }
}