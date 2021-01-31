package i.am.shiro.amai.widget

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import com.annimon.stream.function.Consumer

internal class TapDetector(context: Context?, tapListener: Consumer<MotionEvent?>) {

    private val gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            tapListener.accept(e)
            return true
        }
    })

    fun onTouchEvent(ev: MotionEvent?) {
        gestureDetector.onTouchEvent(ev)
    }
}