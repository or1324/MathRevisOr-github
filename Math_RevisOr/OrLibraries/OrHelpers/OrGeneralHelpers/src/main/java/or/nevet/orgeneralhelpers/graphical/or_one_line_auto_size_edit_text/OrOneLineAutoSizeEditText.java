package or.nevet.orgeneralhelpers.graphical.or_one_line_auto_size_edit_text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;

import or.nevet.orgeneralhelpers.R;

public abstract class OrOneLineAutoSizeEditText extends androidx.appcompat.widget.AppCompatEditText {

    private final static float fontSizeIncrease = 0.1f;
    private String hint = "";

    public OrOneLineAutoSizeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMaxLines(1);
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.OrOneLineAutoSizeEditText,
                0, 0);
        int inputType;
        try {
            hint = array.getString(R.styleable.OrOneLineAutoSizeEditText_orHint);
            if (hint == null)
                hint = "";
            inputType = array.getInt(R.styleable.OrOneLineAutoSizeEditText_orInputType, 1);
            setOrHint(hint);
        } finally {
            array.recycle();
        }
        init(inputType);
    }

    public OrOneLineAutoSizeEditText(Context context, int inputType) {
        super(context);
        setMaxLines(1);
        init(inputType);
    }

    //Use this instead of setHint. Never use setHint.
    public void setOrHint(String hint) {
        this.hint = hint;
        setHint(hint);
        float fontSize = calculateFontSizeToFitHint();
        setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
    }

    //Use this instead of getHint if you want the hint even if it does not show up on the screen.
    public String getOrHint() {
        return hint;
    }

    protected void init(int inputType) {
        //strict this editText to one line, and makes it scrollable horizontally. Allow only text or numbers.
        if (inputType == InputType.TYPE_CLASS_TEXT || inputType == InputType.TYPE_CLASS_NUMBER || inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            setInputType(inputType);
            if (inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD)
                setTransformationMethod(new PasswordTransformationMethod());
        }
        //If not text and not numbers, make it text.
        else
            setInputType(InputType.TYPE_CLASS_TEXT);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //When the size changes, there is more space for the height of the text, so we can make the text bigger.
        matchFontSizeToCurrentInputAndSize();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        //font size should be calculated again due to padding changes
        matchFontSizeToCurrentInputAndSize();
    }

    protected void matchFontSizeToCurrentInputAndSize() {
        float newFontSize;
        //if there is no text and there is no hint, the font should be by height and not by hint. This is not really needed because in this case both sizes will be the same, but it is ok.
        //if there is no hint and there is text, the font should be at the size of the text, because that is what will be on the screen.
        //if there is hint and there is text, the font should be at the size of the text, because that is what will be on the screen.
        //if there is hint and there is no text, the font should be at the size of the hint, because that is what will be on the screen.
        if (getHint() != null && !getHint().toString().isEmpty() && (getText() == null || getText().toString().isEmpty()))
            newFontSize = calculateFontSizeToFitHint();
        else
            newFontSize = calculateFontSizeToFitHeight();
        setTextSize(TypedValue.COMPLEX_UNIT_PX, newFontSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //fix cursor not in right place some times (because of changes that the class do without updating the layout)
        requestLayout();
    }

    //hint is not scrollable, so we need to consider both the height and the width.
    private float calculateFontSizeToFitHint() {
        float newFontSizeWidth = calculateFontSizeToFitHintWidth();
        float newFontSizeHeight = calculateFontSizeToFitHeight();
        return Math.min(newFontSizeWidth, newFontSizeHeight);
    }

    private float calculateFontSizeToFitHeight() {
        Paint p = getPaint();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        float allowedFontSize = 0f;
        Paint measurePaint = new Paint();
        measurePaint.setTextSize(p.getTextSize());
        measurePaint.setTypeface(p.getTypeface());
        float fontHeight = measurePaint.getFontMetrics().bottom - measurePaint.getFontMetrics().top;
        if (height > 0) {
            if (fontHeight < height) {
                while (fontHeight < height) {
                    allowedFontSize = measurePaint.getTextSize();
                    measurePaint.setTextSize(measurePaint.getTextSize() + fontSizeIncrease);
                    fontHeight = measurePaint.getFontMetrics().bottom - measurePaint.getFontMetrics().top;
                }
            } else {
                while (fontHeight > height) {
                    allowedFontSize = measurePaint.getTextSize();
                    measurePaint.setTextSize(measurePaint.getTextSize() - fontSizeIncrease);
                    fontHeight = measurePaint.getFontMetrics().bottom - measurePaint.getFontMetrics().top;
                }
            }
            return allowedFontSize;
        }
        return getTextSize();
    }

    private float calculateFontSizeToFitHintWidth() {
        CharSequence text = getText();
        CharSequence hintCharSeq = getOrHint();
        if (text != null && hintCharSeq != null)
            if (text.toString().isEmpty() && !hintCharSeq.toString().isEmpty()) {
                Paint p = getPaint();
                int width = getWidth() - getPaddingLeft() - getPaddingRight();
                float allowedFontSize = 0f;
                Paint measurePaint = new Paint();
                measurePaint.setTextSize(p.getTextSize());
                measurePaint.setTypeface(p.getTypeface());
                String hint = hintCharSeq.toString();
                Rect bounds = new Rect();
                measurePaint.getTextBounds(hint, 0, hint.length(), bounds);
                float fontWidth = bounds.width();
                if (width > 0) {
                    if (fontWidth > width) {
                        while (fontWidth > width) {
                            allowedFontSize = measurePaint.getTextSize();
                            measurePaint.setTextSize(measurePaint.getTextSize() - fontSizeIncrease);
                            measurePaint.getTextBounds(hint, 0, hint.length(), bounds);
                            //multiplies because if it doesn't, it does not work accurately.
                            fontWidth = bounds.width()*(1.1f);
                        }
                    } else {
                        while (fontWidth < width) {
                            allowedFontSize = measurePaint.getTextSize();
                            measurePaint.setTextSize(measurePaint.getTextSize() + fontSizeIncrease);
                            measurePaint.getTextBounds(hint, 0, hint.length(), bounds);
                            //multiplies because if it doesn't, it does not work accurately.
                            fontWidth = bounds.width()*(1.1f);
                        }
                    }
                    return allowedFontSize;
                }
            }
        return getTextSize();
    }
}
