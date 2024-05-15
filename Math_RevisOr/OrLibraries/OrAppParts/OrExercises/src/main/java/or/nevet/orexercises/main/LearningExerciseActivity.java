package or.nevet.orexercises.main;

import android.window.SplashScreen;

import or.nevet.mathrevisorusermanager.UserManager;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.high_level.math_operations.MathOperationWithLearning;
import or.nevet.orexercises.helpers.visual.ExerciseActivity;
import or.nevet.orexercises.helpers.logic.data_objects.exceptions.StopExerciseException;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orgeneralhelpers.background_running_related.BackgroundRunningHelper;
import or.nevet.orgeneralhelpers.tts.OrTTS;

abstract class LearningExerciseActivity extends ExerciseActivity {
    public void startDoingExercises(LearnedExercises exercises) {
        OrTTS.createTTS(this, (succeeded) -> {
            if (succeeded) {
                createMathOperation(exercises);
                try {
                    mathOperation.startFirstExercise();
                } catch (StopExerciseException ignored) {
                }
            }
        });
    }

    protected abstract void createMathOperation(LearnedExercises exercises);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BackgroundRunningHelper.runCodeInBackgroundAsync(() -> {
            try {
                UserManager.uploadUser();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public boolean getIsInWaitingForUserToClickOnNextProcess() {
        return ((MathOperationWithLearning)mathOperation).getIsInWaitingProcess();
    }
}
