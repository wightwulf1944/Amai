package i.am.shiro.amai

import android.content.SharedPreferences
import androidx.core.content.edit

private const val IS_FIRST_RUN = "isFirstRun"
private const val STORAGE_PATH = "storagePath"

// TODO consider change to new preferences storage
class AmaiPreferences(private val sharedPreferences: SharedPreferences) {

    var isFirstRun: Boolean
        get() = sharedPreferences.getBoolean(IS_FIRST_RUN, true)
        set(value) = sharedPreferences.edit {
            putBoolean(IS_FIRST_RUN, value)
        }

    var storagePath: String?
        get() = sharedPreferences.getString(STORAGE_PATH, null)
        set(value) = sharedPreferences.edit {
            putString(STORAGE_PATH, value)
        }
}