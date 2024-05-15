package or.nevet.orexercises.main;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.high_level.math_operations.MathOperationWithLearningAndScore;
import or.nevet.orexercises.helpers.visual.AppGraphics;
import or.nevet.orgeneralhelpers.tts.OrTTS;
import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.InternetManager;
import or.nevet.orgeneralhelpers.background_running_related.RunOnEachInterval;
import or.nevet.orgeneralhelpers.background_running_related.TimerReference;

public class PracticeExerciseActivityWithScore extends LearningExerciseActivity {

    private TimerReference timerReference;
    private AppCompatImageView internet;
    private RunOnEachInterval runOnEachInterval;
    private ConstraintLayout scoreLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppGraphics.initScoreExerciseActivity(this);
        runOnEachInterval = new RunOnEachInterval(1, RunOnEachInterval.BlockingMethod.RunOnUi);
        updateInternetState();
    }

    private void updateInternetState() {
        timerReference = runOnEachInterval.startRunning(() -> {
            if (InternetManager.isConnectedToInternet(PracticeExerciseActivityWithScore.this)) {
                internet.setVisibility(View.GONE);
            } else {
                internet.setVisibility(View.VISIBLE);
            }
        }, GeneralConstants.secondInMillis);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        runOnEachInterval.stop(timerReference);
    }

    @Override
    protected void createMathOperation(LearnedExercises exercises) {
        mathOperation = new MathOperationWithLearningAndScore(this, OrTTS.getTTSHelper(), exercises);
    }

    public void setScoreLayout(ConstraintLayout layout) {
        this.scoreLayout = layout;
    }

    public void setInternetImageView(AppCompatImageView internetImageView) {
        this.internet = internetImageView;
    }

    public ConstraintLayout getScoreLayout() {
        return scoreLayout;
    }
}
