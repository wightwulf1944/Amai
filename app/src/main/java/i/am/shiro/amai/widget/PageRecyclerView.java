package i.am.shiro.amai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.annimon.stream.function.IntConsumer;

import i.am.shiro.amai.R;

import static i.am.shiro.amai.util.ViewGroupXKt.forEach;

public final class PageRecyclerView extends RecyclerView {

    private static final float FLINGING_CHILD_SCALE = 0.9f;

    private static final int COOLDOWN = 1000;

    private static final int COOLDOWN_TURBO = 500;

    private final TapDetector tapDetector;

    private final int pagerTapZoneWidth;

    private IntConsumer onPageScrollListener;

    private long nextNotifyTime;

    private int snapOffset;

    private float childScale = 1;

    public PageRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        tapDetector = new TapDetector(context, this::onSingleTapUp);
        pagerTapZoneWidth = context.getResources()
            .getDimensionPixelSize(R.dimen.tap_zone_width);
    }

    public void setOnPageScrollListener(IntConsumer listener) {
        onPageScrollListener = listener;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (dx != 0) {
            int extent = computeHorizontalScrollExtent();
            int currentOffset = computeHorizontalScrollOffset();
            int currentPosition = Math.round((float) currentOffset / (float) extent);
            onPageScrollListener.accept(currentPosition + 1);

            int targetOffset = currentPosition * extent;
            snapOffset = targetOffset - currentOffset;
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == SCROLL_STATE_IDLE) {
            smoothScrollBy(snapOffset, 0);

            forEach(this, child ->
                child.animate()
                    .scaleX(1)
                    .scaleY(1)
            );

            childScale = 1;
        } else if (state == SCROLL_STATE_DRAGGING) {
            forEach(this, child ->
                child.animate()
                    .scaleX(FLINGING_CHILD_SCALE)
                    .scaleY(FLINGING_CHILD_SCALE)
            );

            childScale = FLINGING_CHILD_SCALE;
        }
    }

    @Override
    public void onChildAttachedToWindow(@NonNull View child) {
        child.setScaleX(childScale);
        child.setScaleY(childScale);
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
