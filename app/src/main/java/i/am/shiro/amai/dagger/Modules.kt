package i.am.shiro.amai.dagger

import android.app.Application
import android.preference.PreferenceManager
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.Reusable
import i.am.shiro.amai.AmaiPreferences
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.viewmodel.factory.ViewModelFactory
import javax.inject.Singleton

@Module
object AmaiModule {

    @Provides
    @Reusable
    fun provideVmFactory(database: AmaiDatabase, preferences: AmaiPreferences) =
        ViewModelFactory(database, preferences)

    @Provides
    @Singleton
    fun provideDb(application: Application) =
        Room.databaseBuilder(application, AmaiDatabase::class.java, "amai")
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()

    @Provides
    @Reusable
    fun providePreferences(application: Application) =
        AmaiPreferences(PreferenceManager.getDefaultSharedPreferences(application))
}