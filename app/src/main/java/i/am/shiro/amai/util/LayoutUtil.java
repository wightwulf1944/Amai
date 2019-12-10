package i.am.shiro.amai.util;

import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.function.Consumer;

public final class LayoutUtil {

    public static void forEachChild(ViewGroup parent, Consumer<View> childConsumer) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            childConsumer.accept(child);
        }
    }
}
