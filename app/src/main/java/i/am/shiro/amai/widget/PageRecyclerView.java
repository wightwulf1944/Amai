package i.am.shiro.amai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import i.am.shiro.amai.R;

public final class PageRecyclerView extends RecyclerView {

    private static final int COOLDOWN = 1000;

    private static final int COOLDOWN_TURBO = 500;

    private final TapDetector tapDetector;

    private final int pagerTapZoneWidth;

    private long nextNotifyTime;

    private boolean wasScrolled = false;

    public PageRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        tapDetector = new TapDetector(context, this::onSingleTapUp);
        pagerTapZoneWidth = context.getResources()
            .getDimensionPixelSize(R.dimen.tap_zone_width);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        wasScrolled = dx != 0;
        super.onScrolled(dx, dy);
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (state == RecyclerView.SCROLL_STATE_IDLE && wasScrolled) {
            wasScrolled = false;
            int extent = computeHorizontalScrollExtent();
            int currentOffset = computeHorizontalScrollOffset();
            int currentPosition = Math.round((float) currentOffset / (float) extent);
            int targetOffset = currentPosition * extent;
            smoothScrollBy(targetOffset - currentOffset, 0);
        }
        super.onScrollStateChanged(state);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        tapDetector.onTouchEvent(e);
        return super.onTouchEvent(e);
    }

    // TODO it might be possible to improve this by using postDelayed()
    // TODO this should be moved to the fragment root view so it works regardless of view focus
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Runnable eventHandler;
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            eventHandler = this::previousPage;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            eventHandler = this::nextPage;
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

    private void onSingleTapUp(MotionEvent e) {
        if (e.getX() < pagerTapZoneWidth) {
            previousPage();
        } else if (e.getX() > getWidth() - pagerTapZoneWidth) {
            nextPage();
        }
    }

    private void nextPage() {
        int extent = computeHorizontalScrollExtent();
        int currentPosition = computeHorizontalScrollOffset() / extent;
        int maxPosition = computeHorizontalScrollRange() / extent;
        if (currentPosition < maxPosition) {
            smoothScrollToPosition(currentPosition + 1);
        }
    }

    private void previousPage() {
        int currentPosition = computeHorizontalScrollOffset() / computeHorizontalScrollExtent();
        if (currentPosition > 0) {
            smoothScrollToPosition(currentPosition - 1);
        }
    }
}
