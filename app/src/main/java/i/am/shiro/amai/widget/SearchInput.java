package i.am.shiro.amai.widget;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.annimon.stream.Optional;
import com.annimon.stream.function.Consumer;
import com.google.android.material.textfield.TextInputEditText;

public final class SearchInput extends TextInputEditText {

    private Consumer<String> onSubmitListener;

    private Consumer<String> onTextChangedListener;

    public SearchInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        setImeOptions(EditorInfo.IME_ACTION_SEARCH);
    }

    public void setOnSubmitListener(Consumer<String> onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
    }

    public void setOnTextChangedListener(Consumer<String> onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
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
            Optional.ofNullable(getText())
                    .map(Editable::toString)
                    .ifPresent(onSubmitListener);
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

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (onTextChangedListener != null) onTextChangedListener.accept(text.toString());
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
