package i.am.shiro.amai.widget;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.annimon.stream.function.Consumer;

final class TapDetector {

    private final GestureDetector gestureDetector;

    TapDetector(Context context, Consumer<MotionEvent> tapListener) {
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                tapListener.accept(e);
                return true;
            }
        });
    }

    boolean onTouchEvent(MotionEvent ev) {
        return gestureDetector.onTouchEvent(ev);
    }
}
