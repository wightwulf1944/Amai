package i.am.shiro.amai.util

import android.app.Service
import android.content.Context
import android.content.Intent

inline fun <reified T : Service> Context.startLocalService() {
    val intent = Intent(this, T::class.java)
    startService(intent)
}