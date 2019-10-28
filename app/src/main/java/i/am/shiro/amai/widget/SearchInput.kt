package i.am.shiro.amai.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.google.android.material.textfield.TextInputEditText
import timber.log.Timber

class SearchInput(context: Context, attrs: AttributeSet) : TextInputEditText(context, attrs) {

    var onSubmitListener: (String) -> Unit = {}

    // nullable necessary
    var onTextChangedListener: ((String) -> Unit)? = null

    init {
        Timber.wtf("RABBIT 123")
        imeOptions = EditorInfo.IME_ACTION_SEARCH
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss()
            return true
        }

        return super.onKeyPreIme(keyCode, event)
    }

    override fun onEditorAction(actionCode: Int) {
        if (actionCode == EditorInfo.IME_ACTION_SEARCH) {
            dismiss()
            text?.toString()?.let { onSubmitListener(it) }
        }

        super.onEditorAction(actionCode)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        if (!focused) {
            hideKeyboard()
        }

        super.onFocusChanged(focused, direction, previouslyFocusedRect)
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        onTextChangedListener?.invoke(text.toString())
    }

    private fun dismiss() {
        hideKeyboard()
        clearFocus()
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
