package or.nevet.orexercises.helpers.logic.exercise_mechanism.high_level.math_operations;

import android.view.View;

import or.nevet.orexercises.R;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.LearningProcessHelper;
import or.nevet.orexercises.helpers.logic.data_objects.exceptions.StopExerciseException;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.exercise_helpers.EnhancedExerciseHelperWithLearningMathOperationActivity;
import or.nevet.orexercises.helpers.visual.ExerciseActivity;
import or.nevet.orgeneralhelpers.background_running_related.BackgroundRunningHelper;
import or.nevet.orgeneralhelpers.tts.OrTTS;

//Everytime someone calls a method from here, he should make sure that he calls from a background thread, because this class deals with the background logic.
public class MathOperationWithLearning extends MathOperation {
    protected volatile int learningProcess;
    //signals when user exits the exercise.
    private final OrTTS.TTSHelper tts;
    LearningProcessHelper learningProcessHelper;

    boolean isWaiting = false;

    public enum LearningState {
        Success, Failure
    }

    private LearningState learningState;

    public MathOperationWithLearning(ExerciseActivity activity, OrTTS.TTSHelper tts, LearnedExercises exercises) {
        super(activity, exercises);
        this.tts = tts;
        learningProcessHelper = new LearningProcessHelper(this);
    }

    @Override
    protected EnhancedExerciseHelperWithLearningMathOperationActivity getExerciseHelper() {
        return (EnhancedExerciseHelperWithLearningMathOperationActivity) super.getExerciseHelper();
    }

    @Override
    protected EnhancedExerciseHelperWithLearningMathOperationActivity createExerciseHelper() {
        return new EnhancedExerciseHelperWithLearningMathOperationActivity(this);
    }

    public int getLearningProcess() {
        return learningProcess;
    }

    public void setLearningProcess(int value) {
        learningProcess = value;
    }

    @Override
    protected void createFirstExercise() {
        getExerciseHelper().restoreSavedExercisesState();
        if (learningProcess != 0) {
            learningState = LearningState.Failure;
            showLearning();
        } else {
            learningState = LearningState.Success;
            hideLearning();
        }
    }

    @Override
    protected void createNextExercise() {
        if (learningProcess != 0) {
            learningState = LearningState.Failure;
            showLearning();
            chooseWhatToDoInFailure();
        } else {
            learningState = LearningState.Success;
            hideLearning();
            super.createNextExercise();
        }
    }

    private void chooseWhatToDoInFailure() {
        if (learningProcess < 10) {
            //10 times of revising before the end
            //twice in 3 rounds gets a random exercise and then gets the one that needs practicing, like that for 10 times
            if (learningProcess % 3 == 0) {
                getExerciseHelper().reviseTheExerciseAgain();
            } else
                getExerciseHelper().generateRandomExercise();
        } else {
            //the first 10 times of revising
            //once gets a known exercise and then gets the one that needs practicing, like that for 10 times
            if (learningProcess % 2 == 0) {
                getExerciseHelper().reviseTheExerciseAgain();
            } else
                getExerciseHelper().generateKnownExercise();
        }
    }

    private void showLearning() {
        BackgroundRunningHelper.runCodeOnUiThread(() -> getExerciseActivity().findViewById(R.id.learning).setVisibility(View.VISIBLE));
    }

    private void hideLearning() {
        BackgroundRunningHelper.runCodeOnUiThread(() -> getExerciseActivity().findViewById(R.id.learning).setVisibility(View.GONE));
    }

    private void wrongDuringSuccess() {
        wrong();
        learningProcess = 20;
        learningProcessHelper.writeLearn();
        getExerciseHelper().currentExerciseNeedsRevising();
    }

    protected void wrong() {
        readExercise();
        showAnswer(getExerciseHelper().getCorrectAnswer());
    }

    private void right() {
        getExerciseHelper().exerciseWasSucceeded();
        try {
            startNextExercise();
        } catch (StopExerciseException ignored) {
        }
    }

    private void readExercise() {
        getExerciseHelper().readExercise();
    }

    //returns whether the user was right
    protected boolean moveToNextExercise(Runnable runAfterSignal) {
        if (learningState == LearningState.Failure)
            learningProcessHelper.nextLearn();
        if (getExerciseHelper().isTheAnswerRight(getExerciseActivity().getExerciseKeyboard().getInputView())) {
            userWasRight(() -> right());
            return true;
        } else {
            if (learningState == LearningState.Failure)
                wrong();
            else
                wrongDuringSuccess();
            isWaiting = true;
            return false;
        }
    }

    //returns whether the user was right
    public boolean moveToNextState(Runnable runAfterSignal) throws StopExerciseException {
        if (isWaiting) {
            isWaiting = false;
            startNextExercise();
            return false;
        } else
            return super.moveToNextState(runAfterSignal);
    }

    protected void showAnswer(int answer) {
        BackgroundRunningHelper.runCodeOnUiThread(() -> {
            getExerciseActivity().showAnswer(answer);
        });
    }

    protected void readText(String text) {
        tts.readText(text);
    }

    public boolean getIsInWaitingProcess() {
        return isWaiting;
    }

}
