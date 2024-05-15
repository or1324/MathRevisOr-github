package or.nevet.orexercises.helpers.logic.interfaces;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import or.nevet.orexercises.helpers.visual.exercise_keyboard.ExerciseKeyboard;


public interface ExerciseScreen {
    void exitScreen(View v);
    void answerExercise();
    void nextAfterAnswerShown();
    ConstraintLayout getBackground();
    void setBackground(ConstraintLayout background);
    void startTimer();
    void showAnswer(int correctAnswer);
    void setGiveUp(AppCompatImageButton giveUp);
    AppCompatImageButton getGiveUp();
    ExerciseKeyboard getExerciseKeyboard();
    boolean getIsInWaitingForUserToClickOnNextProcess();
}
