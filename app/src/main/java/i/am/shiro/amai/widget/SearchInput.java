package i.am.shiro.amai.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import com.annimon.stream.function.Consumer;

public final class SearchInput extends AppCompatEditText {

    private Consumer<String> onSubmitListener;

    public SearchInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        setImeOptions(EditorInfo.IME_ACTION_SEARCH);
    }

    public void setOnSubmitListener(Consumer<String> onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        }

        return super.onKeyPreIme(keyCode, event);
    }

    @Override
    public void onEditorAction(int actionCode) {
        if (actionCode == EditorInfo.IME_ACTION_SEARCH) {
            dismiss();
            onSubmitListener.accept(getText().toString());
        }

        super.onEditorAction(actionCode);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (!focused) {
            hideKeyboard();
        }

        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    private void dismiss() {
        hideKeyboard();
        clearFocus();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
    }
}
