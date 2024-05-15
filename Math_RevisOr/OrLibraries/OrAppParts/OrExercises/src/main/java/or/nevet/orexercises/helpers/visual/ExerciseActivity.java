package or.nevet.orexercises.helpers.visual;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import or.nevet.orexercises.R;
import or.nevet.orexercises.helpers.logic.data_objects.exceptions.StopExerciseException;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.high_level.math_operations.MathOperation;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.learned_exercise_manager.LearnedExerciseManager;
import or.nevet.orexercises.helpers.logic.interfaces.ExerciseScreen;
import or.nevet.orexercises.helpers.visual.exercise_keyboard.ExerciseKeyboard;
import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;
import or.nevet.orgeneralhelpers.music_related.InteractiveMusicActivity;
import or.nevet.orgeneralhelpers.background_running_related.BackgroundRunningHelper;
import or.nevet.orgeneralhelpers.background_running_related.RunOnEachInterval;
import or.nevet.orgeneralhelpers.background_running_related.TimerReference;
import or.nevet.orgeneralhelpers.music_related.InteractiveMusicSubActivity;

//Everytime someone calls a method from here, he should make sure that he calls from the UI thread, because this class deals with the UI.
public abstract class ExerciseActivity extends InteractiveMusicSubActivity implements ExerciseScreen {
    public MathOperation mathOperation;
    private LearnedExercises exercises;
    private AppCompatTextView timeView;
    private TimerReference timerReference;
    private RunOnEachInterval runOnEachInterval;
    private AppCompatTextView scoreView;
    protected ExerciseKeyboard keyboard;
    private ConstraintLayout background;
    private AppCompatImageButton giveUp;
    private int time = 0;
    //signals when user exits the exercise.
    public volatile boolean stop;
    private boolean wasTheExerciseAnswered = false;
    private boolean isOpenedForTheFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        runOnEachInterval = new RunOnEachInterval(1, RunOnEachInterval.BlockingMethod.RunOnUi);
        getExercises();
        AppGraphics.initExerciseActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isOpenedForTheFirstTime) {
            isOpenedForTheFirstTime = false;
            startDoingExercises(exercises);
        }
    }

    protected void getExercises() {
        exercises = getIntent().getExtras().getParcelable(GeneralExerciseConstants.exercisesExtrasName);
        keyboard = new ExerciseKeyboard(getWindow().getDecorView(), exercises.getExerciseSign(), this);
    }

    protected abstract void startDoingExercises(LearnedExercises exercises);

    public void onBackPressed() {
        stop = true;
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    protected void saveData() {
        LearnedExerciseManager.saveSpecificLearnedExercises(this, exercises);
    }

    public void startTimer() {
        wasTheExerciseAnswered = false;
        time = GeneralExerciseConstants.timeOfQuestion;
        timerReference = runOnEachInterval.startRunning(() -> {
            if (!stop) {
                if (time > 0) {
                    timeView.setText(String.valueOf(time));
                    time--;
                } else {
                    answerExercise();
                }
            }
        }, GeneralConstants.secondInMillis);
    }

    @Override
    public void showAnswer(int correctAnswer) {
        runOnEachInterval.stop(timerReference);
        timeView.setText("");
        keyboard.showAnswer(correctAnswer);
    }

    @Override
    public void exitScreen(View v) {
        stop = true;
        this.onClick(v);
    }

    @Override
    public void answerExercise() {
        if (!wasTheExerciseAnswered) {
            wasTheExerciseAnswered = true;
            runOnEachInterval.stop(timerReference);
            moveToNextState();
        }
    }

    protected void moveToNextState() {
        BackgroundRunningHelper.runCodeInBackgroundAsync(() ->{
            try {
                mathOperation.moveToNextState(null);
            } catch (StopExerciseException ignored) {
            }
        });
    }

    @Override
    public void nextAfterAnswerShown() {
        BackgroundRunningHelper.runCodeInBackgroundAsync(() -> {
            try {
                mathOperation.moveToNextState(null);
            } catch (StopExerciseException ignored) {
            }
        });
    }

    public AppCompatTextView getScoreView() {
        return scoreView;
    }

    public void setTimeView(AppCompatTextView timeView) {
        this.timeView = timeView;
    }

    public void setScoreView(AppCompatTextView scoreView) {
        this.scoreView = scoreView;
    }

    @Override
    public AppCompatImageButton getGiveUp() {
        return giveUp;
    }

    @Override
    public void setGiveUp(AppCompatImageButton giveUp) {
        this.giveUp = giveUp;
    }

    @Override
    public ConstraintLayout getBackground() {
        return background;
    }

    @Override
    public void setBackground(ConstraintLayout background) {
        this.background = background;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        runOnEachInterval.stop(timerReference);
        keyboard.hideKeyboard();
    }

    public ExerciseKeyboard getExerciseKeyboard() {
        return keyboard;
    }

    @Override
    public AppCompatImageButton getMusicButton() {
        return keyboard.getMusic();
    }

    @Override
    public boolean getIsInWaitingForUserToClickOnNextProcess() {
        return false;
    }
}
