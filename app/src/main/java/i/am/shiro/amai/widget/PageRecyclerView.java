package i.am.shiro.amai.widget;

import android.content.Context;
import android.os.Handler;
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

    private static final int TURBO_INTERVAL = 350;

    private static final int TURBO_DELAY = TURBO_INTERVAL * 2;

    private static final float FLINGING_CHILD_SCALE = 0.9f;

    private final TapDetector tapDetector;

    private final int pagerTapZoneWidth;

    private final Handler pageFlipHandler = new Handler();

    private IntConsumer onPageScrollListener;

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
        if (dx == 0) return;

        int extent = computeHorizontalScrollExtent();
        int currentOffset = computeHorizontalScrollOffset();
        int currentPosition = Math.round((float) currentOffset / (float) extent);
        onPageScrollListener.accept(currentPosition + 1);

        int targetOffset = currentPosition * extent;
        snapOffset = targetOffset - currentOffset;
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

    private void onSingleTapUp(MotionEvent e) {
        if (e.getX() < pagerTapZoneWidth) {
            flipPreviousPage();
        } else if (e.getX() > getWidth() - pagerTapZoneWidth) {
            flipNextPage();
        }
    }

    // TODO this should be moved to the fragment root view so it works regardless of view focus
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (event.getRepeatCount() == 0) {
                flipPreviousPage();
                pageFlipHandler.removeCallbacksAndMessages(null);
                pageFlipHandler.postDelayed(this::flipPreviousPageTurbo, TURBO_DELAY);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (event.getRepeatCount() == 0) {
                flipNextPage();
                pageFlipHandler.removeCallbacksAndMessages(null);
                pageFlipHandler.postDelayed(this::flipNextPageTurbo, TURBO_DELAY);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            pageFlipHandler.removeCallbacksAndMessages(null);
            return true;
        }
        return false;
    }

    private void flipPreviousPageTurbo() {
        flipPreviousPage();
        pageFlipHandler.postDelayed(this::flipPreviousPageTurbo, TURBO_INTERVAL);
    }

    private void flipNextPageTurbo() {
        flipNextPage();
        pageFlipHandler.postDelayed(this::flipNextPageTurbo, TURBO_INTERVAL);
    }

    private void flipNextPage() {
        int extent = computeHorizontalScrollExtent();
        int currentPosition = computeHorizontalScrollOffset() / extent;
        int maxPosition = computeHorizontalScrollRange() / extent;
        if (currentPosition < maxPosition) {
            smoothScrollToPosition(currentPosition + 1);
        }
    }

    private void flipPreviousPage() {
        int currentPosition = computeHorizontalScrollOffset() / computeHorizontalScrollExtent();
        if (currentPosition > 0) {
            smoothScrollToPosition(currentPosition - 1);
        }
    }
}
