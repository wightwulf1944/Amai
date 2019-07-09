package i.am.shiro.amai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public final class PageRecyclerView extends RecyclerView {

    private static final int COOLDOWN = 1000;

    private static final int COOLDOWN_TURBO = 500;

    private long nextNotifyTime;

    public PageRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Runnable eventHandler;
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            eventHandler = this::onVolumeDown;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            eventHandler = this::onVolumeUp;
        } else {
            return false;
        }

        if (event.getRepeatCount() == 0) {
            eventHandler.run();
            nextNotifyTime = event.getEventTime() + COOLDOWN;
        } else if (event.getEventTime() >= nextNotifyTime) {
            eventHandler.run();
            nextNotifyTime = event.getEventTime() + COOLDOWN_TURBO;
        }
        return true;
    }

    private void onVolumeUp() {
        int extent = computeHorizontalScrollExtent();
        int currentPosition = computeHorizontalScrollOffset() / extent;
        int maxPosition = computeHorizontalScrollRange() / extent;
        if (currentPosition < maxPosition) {
            smoothScrollToPosition(currentPosition + 1);
        }
    }

    private void onVolumeDown() {
        int currentPosition = computeHorizontalScrollOffset() / computeHorizontalScrollExtent();
        if (currentPosition > 0) {
            smoothScrollToPosition(currentPosition - 1);
        }
    }
}
