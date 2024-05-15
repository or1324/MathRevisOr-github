package or.nevet.orexercises.helpers.visual.exercise_keyboard;

import android.view.MotionEvent;
import android.view.View;

import or.nevet.orexercises.R;
import or.nevet.orgeneralhelpers.background_running_related.BackgroundRunningHelper;
import or.nevet.orgeneralhelpers.graphical.or_one_line_auto_size_edit_text.unfocusable.OrOneLineAutoSizeUnfocusableCursorEditText;

class KeyboardFunctionality {
    private boolean isPressingDelete = false;
    private Thread deleteThread;
    private boolean areButtonsEnabled;
    private View keyboardView;
    
    KeyboardFunctionality(View keyboardView) {
        this.keyboardView = keyboardView;
        areButtonsEnabled = true;
        registerOnClick();
    }
    public void registerOnClick() {
        keyboardView.findViewById(R.id.delete).setOnClickListener(v -> delete());
        keyboardView.findViewById(R.id.delete).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dealWithLongPressDelete(event);
                //returning false to allow short press delete.
                return false;
            }
        });
        keyboardView.findViewById(R.id.col11).setOnClickListener(v -> write("1"));
        keyboardView.findViewById(R.id.col12).setOnClickListener(v -> write("2"));
        keyboardView.findViewById(R.id.col13).setOnClickListener(v -> write("3"));
        keyboardView.findViewById(R.id.col21).setOnClickListener(v -> write("4"));
        keyboardView.findViewById(R.id.col22).setOnClickListener(v -> write("5"));
        keyboardView.findViewById(R.id.col23).setOnClickListener(v -> write("6"));
        keyboardView.findViewById(R.id.col31).setOnClickListener(v -> write("7"));
        keyboardView.findViewById(R.id.col32).setOnClickListener(v -> write("8"));
        keyboardView.findViewById(R.id.col33).setOnClickListener(v -> write("9"));
        keyboardView.findViewById(R.id.col42).setOnClickListener(v -> write("0"));
    }

    private void dealWithLongPressDelete(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isPressingDelete = true;
            waitHalfSecondAndThenStartDeleting();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            isPressingDelete = false;
            if (deleteThread != null)
                deleteThread.interrupt();
        }
    }

    private void waitHalfSecondAndThenStartDeleting() {
        deleteThread = BackgroundRunningHelper.runCodeInBackgroundAsync(() -> {
            try {
                Thread.sleep(500);
                startDeleting();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void startDeleting() throws InterruptedException {
        while (isPressingDelete) {
            delete();
            Thread.sleep(40);
        }
    }

    private void write(String letter) {
        if (areButtonsEnabled) {
            OrOneLineAutoSizeUnfocusableCursorEditText editText = keyboardView.findViewById(R.id.input);
            editText.setText(editText.getString() + letter);
        }
    }

    private void delete() {
        if (areButtonsEnabled) {
            OrOneLineAutoSizeUnfocusableCursorEditText editText = keyboardView.findViewById(R.id.input);
            if (editText.getString().length() > 0)
                editText.setText(editText.getString().substring(0, editText.getString().length() - 1));
        }
    }

    public void disableButtons() {
        areButtonsEnabled = false;
    }

    public void enableButtons() {
        areButtonsEnabled = true;
    }
}
