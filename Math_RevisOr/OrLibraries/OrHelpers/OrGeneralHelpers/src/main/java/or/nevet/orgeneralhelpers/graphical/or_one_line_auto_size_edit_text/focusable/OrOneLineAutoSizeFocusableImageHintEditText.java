package or.nevet.orgeneralhelpers.graphical.or_one_line_auto_size_edit_text.focusable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import or.nevet.orgeneralhelpers.R;

public class OrOneLineAutoSizeFocusableImageHintEditText extends OrOneLineAutoSizeFocusableEditText{

    Drawable imageHint;
    boolean isFocused = false;

    public OrOneLineAutoSizeFocusableImageHintEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.OrOneLineAutoSizeFocusableImageHintEditText,
                0, 0);
        try {
            imageHint = array.getDrawable(R.styleable.OrOneLineAutoSizeFocusableImageHintEditText_orImageHint);
            setBackground(imageHint);
        } finally {
            array.recycle();
        }
        setOrHint("");
    }

    @Override
    public void setBackgroundResource(int resId) {
        //when setting background resource, android removes the padding, so we need to restore it.
        int pl = getPaddingLeft();
        int pt = getPaddingTop();
        int pr = getPaddingRight();
        int pb = getPaddingBottom();

        super.setBackgroundResource(resId);

        this.setPadding(pl, pt, pr, pb);
    }

    public OrOneLineAutoSizeFocusableImageHintEditText(Context context, int inputType, Drawable imageHint) {
        super(context, inputType);
        this.imageHint = imageHint;
        setBackground(imageHint);
        setOrHint("");
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        //image should be redrawn to fit new bounds.
        if (!isFocused) {
            setBackground(imageHint);
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        isFocused = focused;
        if (focused)
            setBackgroundResource(androidx.appcompat.R.drawable.abc_edit_text_material);
        else if (getText() == null || getText().toString().isEmpty())
            setBackground(imageHint);
    }
}
