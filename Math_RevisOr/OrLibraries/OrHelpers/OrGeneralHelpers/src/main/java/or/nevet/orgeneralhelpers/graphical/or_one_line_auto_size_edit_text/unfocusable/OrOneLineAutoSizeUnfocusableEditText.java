package or.nevet.orgeneralhelpers.graphical.or_one_line_auto_size_edit_text.unfocusable;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import or.nevet.orgeneralhelpers.graphical.or_one_line_auto_size_edit_text.OrOneLineAutoSizeEditText;

public class OrOneLineAutoSizeUnfocusableEditText extends OrOneLineAutoSizeEditText {
    public OrOneLineAutoSizeUnfocusableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrOneLineAutoSizeUnfocusableEditText(Context context, int inputType) {
        super(context, inputType);
    }

    @Override
    protected void init(int inputType) {
        super.init(inputType);
        setFocusable(NOT_FOCUSABLE);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        //I change the hint here in addition to changing it in onFocusChanged in case that the text was changed while the view is not focusable, and now there should be hint.
        if (!isFocused() && getHint() != null && !getHint().toString().isEmpty() && (getText() == null || getText().toString().isEmpty())) {
            matchFontSizeToCurrentInputAndSize();
        }
        //need to move the cursor to emulate regular cursor
        scrollToEditTextEnd();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        //I change the hint here before onTextChanged because it looks better. It looks better because if I do not do it, the user sees that the cursor is at the size of the hint. Also, it is not needed to change the size for every new text, because the size is the same while there is text, and the hint is already changing the font at the moment that it is changed in setOrHint.
        if (focused) {
            setHint("");
        }
        else {
            setHint(getOrHint());
        }
        matchFontSizeToCurrentInputAndSize();
    }

    public void emulateNoFocus() {
        onFocusChanged(false, View.FOCUS_DOWN, null);
    }

    public void emulateUserFocus() {
        onFocusChanged(true, View.FOCUS_DOWN, null);
    }

    private void scrollToEditTextEnd() {
        if (getText() != null)
            setSelection(getText().length());
        else
            setSelection(0);
    }
}
