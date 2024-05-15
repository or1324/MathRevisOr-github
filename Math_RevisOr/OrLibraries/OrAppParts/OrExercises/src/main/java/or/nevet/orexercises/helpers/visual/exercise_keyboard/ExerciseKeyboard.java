package or.nevet.orexercises.helpers.visual.exercise_keyboard;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.logic.interfaces.ExerciseScreen;
import or.nevet.orexercises.helpers.visual.AppGraphics;
import or.nevet.orgeneralhelpers.graphical.or_one_line_auto_size_edit_text.unfocusable.OrOneLineAutoSizeUnfocusableCursorEditText;

//Everytime someone calls a method from here, he should make sure that he calls from the UI thread, because this class deals with the UI.
public class ExerciseKeyboard {

    private ConstraintLayout root;
    private AppCompatImageButton music;
    private OrOneLineAutoSizeUnfocusableCursorEditText input;
    private AppCompatImageButton continueNextExercise;
    private AppCompatTextView exerciseView;
    private AppCompatTextView answerView;
    private ConstraintLayout keyboard;
    private AppCompatTextView answerTitle;
    private AppCompatImageButton nextAfterAnswerShown;
    private ConstraintLayout keyboardBackground;
    private final char exerciseSign;
    private Runnable onShowKeyboard = null;
    private final ExerciseScreen exerciseScreen;
    private KeyboardFunctionality keyboardFunctionality;

    public ExerciseKeyboard(View mainView, char sign, ExerciseScreen screen) {
        this.exerciseSign = sign;
        this.exerciseScreen = screen;
        AppGraphics.initExerciseKeyboard(this, mainView);
        keyboardFunctionality = new KeyboardFunctionality(root);
    }

    public void showExercise(LearnedExercise exercise) {
        clearInput();
        exerciseView.setText(exercise.getLeftNumber() + " "+exerciseSign+" "+exercise.getRightNumber());
        showKeyboard();
    }

    public void setRoot(ConstraintLayout root) {
        this.root = root;
    }

    public void showAnswer(int answer) {
        clearInput();
        nextAfterAnswerShown.setVisibility(View.VISIBLE);
        answerTitle.setVisibility(View.VISIBLE);
        hideKeyboard();
        answerView.setVisibility(View.VISIBLE);
        answerTitle.setVisibility(View.VISIBLE);
        answerView.setText(answer+"");
        answerView.setVisibility(View.VISIBLE);
    }

    public void showKeyboard() {
        if (!exerciseScreen.getIsInWaitingForUserToClickOnNextProcess()) {
            if (onShowKeyboard != null)
                onShowKeyboard.run();
            answerView.setVisibility(View.GONE);
            answerTitle.setVisibility(View.GONE);
            nextAfterAnswerShown.setVisibility(View.GONE);
            keyboard.setVisibility(View.VISIBLE);
            input.emulateUserFocus();
        }
    }

    public void hideKeyboard() {
        keyboard.setVisibility(View.GONE);
        input.emulateNoFocus();
    }

    public void setMusic(AppCompatImageButton music) {
        this.music = music;
    }

    public void setInput(OrOneLineAutoSizeUnfocusableCursorEditText input) {
        this.input = input;
    }

    public void setContinueNextExercise(AppCompatImageButton continueNextExercise) {
        this.continueNextExercise = continueNextExercise;
    }

    public void setExerciseView(AppCompatTextView exerciseView) {
        this.exerciseView = exerciseView;
    }

    public void setAnswerView(AppCompatTextView answerView) {
        this.answerView = answerView;
    }

    public void setKeyboard(ConstraintLayout keyboard) {
        this.keyboard = keyboard;
    }

    public void setAnswerTitle(AppCompatTextView answerTitle) {
        this.answerTitle = answerTitle;
    }

    public void setNextAfterAnswerShown(AppCompatImageButton nextAfterAnswerShown) {
        this.nextAfterAnswerShown = nextAfterAnswerShown;
    }

    public OrOneLineAutoSizeUnfocusableCursorEditText getInputView() {
        return input;
    }

    public void disableInput() {
        input.setOnClickListener(null);
    }

    public void enableInput() {
        input.setOnClickListener(v -> showKeyboard());
    }

    public ConstraintLayout getKeyboardBackground() {
        return keyboardBackground;
    }

    public ConstraintLayout getRoot() {
        return root;
    }

    public AppCompatImageButton getMusic() {
        return music;
    }

    public AppCompatImageButton getContinueNextExercise() {
        return continueNextExercise;
    }

    public AppCompatImageButton getNextAfterAnswerShown() {
        return nextAfterAnswerShown;
    }

    public AppCompatTextView getAnswerView() {
        return answerView;
    }

    public AppCompatTextView getAnswerTitle() {
        return answerTitle;
    }

    public void setKeyboardBackground(ConstraintLayout keyboardBackground) {
        this.keyboardBackground = keyboardBackground;
    }

    public void signalToUser(int drawableIdToShow) {
        input.emulateNoFocus();
        keyboardFunctionality.disableButtons();
        input.setBackgroundResource(drawableIdToShow);
    }

    public void stopSignalingToUser() {
        input.setBackgroundResource(androidx.appcompat.R.drawable.abc_edit_text_material);
        input.emulateUserFocus();
        keyboardFunctionality.enableButtons();
        clearInput();
    }

    public void clearInput() {
        input.getText().clear();
    }

    public void setOnShowKeyboardListener(Runnable r) {
        onShowKeyboard = r;
    }
}
