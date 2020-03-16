package i.am.shiro.amai.dagger

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.Reusable
import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.viewmodel.factory.ViewModelFactory
import javax.inject.Singleton

@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provide(application: Application) =
        Room.databaseBuilder(application, AmaiDatabase::class.java, "amai")
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
}

@Module
object ViewModelFactoryModule {
    @Provides
    @Reusable
    fun provide(database: AmaiDatabase) = ViewModelFactory(database)
}