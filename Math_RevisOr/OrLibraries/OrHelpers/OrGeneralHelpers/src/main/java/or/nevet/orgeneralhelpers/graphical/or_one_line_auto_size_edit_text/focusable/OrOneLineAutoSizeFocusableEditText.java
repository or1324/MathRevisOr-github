package or.nevet.orgeneralhelpers.graphical.or_one_line_auto_size_edit_text.focusable;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;

import or.nevet.orgeneralhelpers.graphical.or_one_line_auto_size_edit_text.OrOneLineAutoSizeEditText;

public class OrOneLineAutoSizeFocusableEditText extends OrOneLineAutoSizeEditText {
    public OrOneLineAutoSizeFocusableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrOneLineAutoSizeFocusableEditText(Context context, int inputType) {
        super(context, inputType);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        //I change the hint here before onTextChanged because it looks better. It looks better because if I do not do it, the user sees that the cursor is at the size of the hint.
        if (focused) {
            setHint("");
            //because of the fact that I change the font size while the view gains focus, the keyboard sometimes does not open. So I open it myself. I check for the class because I do not want it to happen to the unfocusable edittext.
            forceShowingKeyboard();
        }
        else {
            setHint(getOrHint());
        }
        matchFontSizeToCurrentInputAndSize();
    }

    private void forceShowingKeyboard() {
        InputMethodManager im = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(this, 0);
    }
}
