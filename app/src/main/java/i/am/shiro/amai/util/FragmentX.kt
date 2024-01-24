package i.am.shiro.amai.util

import android.app.Service
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import i.am.shiro.amai.viewmodel.factory.SavedStateViewModelFactory
import i.am.shiro.amai.viewmodel.factory.ViewModelFactory
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <reified T> Fragment.argument() = object : ReadWriteProperty<Any, T> {

    init {
        if (arguments == null) arguments = Bundle()
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T =
        requireArguments().get(property.name) as T

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        requireArguments().put(property.name, value)
    }
}

inline fun <reified T : Service> Fragment.startLocalService() {
    requireContext().startLocalService<T>()
}

inline fun <reified T : ViewModel> Fragment.amaiViewModels() = viewModels<T> {
    ViewModelFactory()
}

inline fun <reified T : ViewModel> Fragment.amaiStatefulViewModels() = viewModels<T> {
    SavedStateViewModelFactory(this)
}

fun Fragment.dp(value: Int): Int{
    return (value * resources.displayMetrics.density).toInt()
}