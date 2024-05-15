package or.nevet.orgeneralhelpers.graphical.or_one_line_auto_size_edit_text.unfocusable;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.background_running_related.RunOnEachInterval;
import or.nevet.orgeneralhelpers.background_running_related.TimerReference;

public class OrOneLineAutoSizeUnfocusableCursorEditText extends OrOneLineAutoSizeUnfocusableEditText {

    private RunOnEachInterval runOnEachInterval;
    private TimerReference timerReference;
    private boolean theCursorAppears = false;
    private boolean isMyChange;
    //needed for thread synchronization
    private boolean isCursorRunning = false;

    public OrOneLineAutoSizeUnfocusableCursorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrOneLineAutoSizeUnfocusableCursorEditText(Context context, int inputType) {
        super(context, inputType);
    }

    protected void init(int inputType) {
        super.init(inputType);
        runOnEachInterval = new RunOnEachInterval(1, RunOnEachInterval.BlockingMethod.RunOnUi);
        listenToTextChanges();
    }

    private void listenToTextChanges() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isMyChange) {
                    isMyChange = true;
                    if (theCursorAppears)
                        setText(s.toString().concat("|"));
                } else
                    isMyChange = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void startCursor() {
        isCursorRunning = true;
        if (timerReference == null) {
            timerReference = runOnEachInterval.startRunning(() -> {
                if (isCursorRunning) {
                    String text = getText().toString();
                    if (theCursorAppears) {
                        theCursorAppears = false;
                        isMyChange = true;
                        setText(text.substring(0, text.length() - 1));
                    } else {
                        theCursorAppears = true;
                        isMyChange = true;
                        setText(text + "|");
                    }
                }
            }, GeneralConstants.secondInMillis);
        }
    }

    private void stopCursor() {
        isCursorRunning = false;
        runOnEachInterval.stop(timerReference);
        //removes the cursor if it appears
        boolean temp = theCursorAppears;
        theCursorAppears = false;
        if (temp) {
            String text = getText().toString();
            setText(text.substring(0, text.length()-1));
        }
        timerReference = null;
    }

    //Use this instead of getText() to get the text without the cursor.
    public String getString() {
        String text =  getText().toString();
        if (theCursorAppears)
            return text.substring(0, text.length()-1);
        return text;
    }


    @Override
    public void emulateNoFocus() {
        stopCursor();
        super.emulateNoFocus();
    }

    @Override
    public void emulateUserFocus() {
        super.emulateUserFocus();
        startCursor();
    }
}
