package or.nevet.orexercises.helpers.logic.exercise_mechanism.high_level.math_operations;

import or.nevet.orexercises.R;
import or.nevet.orexercises.helpers.logic.data_objects.exceptions.StopExerciseException;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.exercise_helpers.EnhancedExerciseHelper;
import or.nevet.orexercises.helpers.visual.ExerciseActivity;
import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.background_running_related.BackgroundRunningHelper;
import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;

//This class is thread safe. You can use it with as many threads as you want simultaneously with no risk (as long as you use it reasonably). You can call its methods from any thread you want, but calling from main thread is more efficient.
public abstract class MathOperation {
    private final ExerciseActivity activity;
    private final EnhancedExerciseHelper exerciseHelper;
    private final LearnedExercises exercises;
    private boolean wasTheLastAnswerRight = false;
    private enum ExerciseState {
        Executing, SignalsRight
    }
    private ExerciseState state;


    public MathOperation(ExerciseActivity activity, LearnedExercises exercises) {
        this.activity = activity;
        this.exercises = exercises;
        exerciseHelper = createExerciseHelper();
    }

    protected EnhancedExerciseHelper getExerciseHelper() {
        return exerciseHelper;
    }

    protected abstract EnhancedExerciseHelper createExerciseHelper();

    public ExerciseActivity getExerciseActivity() {
        return activity;
    }

    public void startFirstExercise() throws StopExerciseException {
        if (getExerciseActivity().stop)
            throw new StopExerciseException();
        state = ExerciseState.Executing;
        createFirstExercise();
        showExercise();
        activity.startTimer();
    }

    protected void showExercise() {
        BackgroundRunningHelper.runCodeOnUiThread(() -> activity.getExerciseKeyboard().showExercise(exerciseHelper.getCurrentExercise()));
    }

    protected void startNextExercise() throws StopExerciseException {
        if (getExerciseActivity().stop)
            throw new StopExerciseException();
        state = ExerciseState.Executing;
        createNextExercise();
        showExercise();
        activity.startTimer();
    }

    protected abstract void createFirstExercise();

    protected void createNextExercise() {
        exerciseHelper.generateRandomExercise();
    }

    //returns whether the user was right
    public boolean moveToNextState(Runnable runAfterSignal) throws StopExerciseException {
        switch (state) {
            case Executing:
                return moveToNextExercise(runAfterSignal);
            case SignalsRight:
                return false;
            default:
                throw new RuntimeException(UserMessagesConstants.forgotAfterAddingExerciseState);
        }
    }
    //returns whether the user was right
    protected boolean moveToNextExercise(Runnable runAfterSignal) {
        if (exerciseHelper.isTheAnswerRight(activity.getExerciseKeyboard().getInputView())) {
            userWasRight(runAfterSignal);
            return true;
        }
        else {
            userWasWrong(runAfterSignal);
            return false;
        }
    }

    protected void userWasRight(Runnable afterRightSignal) {
        state = ExerciseState.SignalsRight;
        wasTheLastAnswerRight = true;
        BackgroundRunningHelper.runCodeOnUiThreadSync(() -> {
            getExerciseActivity().getExerciseKeyboard().signalToUser(R.drawable.green_button);
            BackgroundRunningHelper.runOnUiAfterSomeTimeAsync(() -> {
                try {
                    if (getExerciseActivity().stop)
                        throw new StopExerciseException();
                    getExerciseActivity().getExerciseKeyboard().stopSignalingToUser();
                    if (afterRightSignal != null)
                        afterRightSignal.run();
                } catch (StopExerciseException ignored) {
                }
                }, GeneralConstants.secondInMillis);
        });
    }

    public boolean wasTheLastAnswerRight() {
        return wasTheLastAnswerRight;
    }

    protected void userWasWrong(Runnable afterWrongSignal) {
        state = ExerciseState.SignalsRight;
        wasTheLastAnswerRight = false;
        BackgroundRunningHelper.runCodeOnUiThreadSync(() -> {
            getExerciseActivity().getExerciseKeyboard().signalToUser(R.drawable.red_button);
            BackgroundRunningHelper.runOnUiAfterSomeTimeAsync(() -> {
                try {
                    if (getExerciseActivity().stop)
                        throw new StopExerciseException();
                    getExerciseActivity().getExerciseKeyboard().stopSignalingToUser();
                    if (afterWrongSignal != null)
                        afterWrongSignal.run();
                } catch (StopExerciseException ignored) {
                }
            }, GeneralConstants.secondInMillis);
        });
    }

    public char getExerciseSign() {
        return exercises.getExerciseSign();
    }

    public LearnedExercises getExercises() {
        return exercises;
    }

}
