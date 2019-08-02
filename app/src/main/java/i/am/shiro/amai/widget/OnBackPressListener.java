package i.am.shiro.amai.widget;

import android.view.KeyEvent;
import android.view.View;

public final class OnBackPressListener implements View.OnKeyListener {

    private final Runnable onBackPressListener;

    public OnBackPressListener(Runnable onBackPressListener) {
        this.onBackPressListener = onBackPressListener;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
            && event.getAction() == KeyEvent.ACTION_DOWN
            && event.getRepeatCount() == 0) {
            onBackPressListener.run();
            return true;
        }
        return false;
    }
}
