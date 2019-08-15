package i.am.shiro.amai.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

import com.annimon.stream.function.Consumer;

public final class LayoutUtil {

    public static <T extends View> T addChild(ViewGroup parent, @LayoutRes int layoutRes) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View childView = inflater.inflate(layoutRes, parent, false);
        parent.addView(childView);
        return (T) childView;
    }

    public static void forEachChild(ViewGroup parent, Consumer<View> childConsumer) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            childConsumer.accept(child);
        }
    }
}
