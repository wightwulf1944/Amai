package i.am.shiro.amai.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

public final class LayoutUtil {

    public static <T extends View> T addChild(ViewGroup parent, @LayoutRes int layoutRes) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View childView = inflater.inflate(layoutRes, parent, false);
        parent.addView(childView);
        return (T) childView;
    }
}
