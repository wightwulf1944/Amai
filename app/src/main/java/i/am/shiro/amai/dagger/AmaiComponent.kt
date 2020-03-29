package i.am.shiro.amai.dagger

import android.app.Activity
import android.app.Application
import android.app.Service
import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Component
import i.am.shiro.amai.AmaiApplication
import i.am.shiro.amai.AmaiPreferences
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.viewmodel.factory.ViewModelFactory
import javax.inject.Singleton

@Singleton
@Component(modules = [AmaiModule::class])
interface AmaiComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AmaiComponent
    }

    val database: AmaiDatabase

    val preferences: AmaiPreferences

    val viewModelFactory: ViewModelFactory
}

val Service.component get() = (application as AmaiApplication).component
val Activity.component get() = (application as AmaiApplication).component
val Fragment.component get() = activity!!.component