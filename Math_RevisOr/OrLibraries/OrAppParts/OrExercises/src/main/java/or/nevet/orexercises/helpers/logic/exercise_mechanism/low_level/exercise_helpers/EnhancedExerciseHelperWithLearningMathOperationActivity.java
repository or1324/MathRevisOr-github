package or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.exercise_helpers;

import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.high_level.math_operations.MathOperationWithLearning;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.learned_exercise_manager.LearnedExerciseManager;

public class EnhancedExerciseHelperWithLearningMathOperationActivity extends EnhancedExerciseHelper{

    private final MathOperationWithLearning operation;

    public EnhancedExerciseHelperWithLearningMathOperationActivity(MathOperationWithLearning operation) {
        super(operation.getExercises(), operation.getExerciseActivity());
        this.operation = operation;
    }

    public void restoreSavedExercisesState() {
        restoreLastExercise();
        restoreRevisingExercise(operation);
    }

    private void restoreRevisingExercise(MathOperationWithLearning operation) {
        try {
            restoreRevisingExercise();
        } catch (Exception e) {
            operation.setLearningProcess(0);
        }
    }

    public void reviseTheExerciseAgain() {
        changeExercise(getNeedsRevisingExercise());
    }

    @Override
    protected void changeRevisingExercise(LearnedExercise newRevisingExercise) {
        super.changeRevisingExercise(newRevisingExercise);
        saveRevisingExercise();
    }

    @Override
    protected void changeExercise(LearnedExercise newExercise) {
        super.changeExercise(newExercise);
        saveLastExercise();
    }

    public void exerciseWasSucceeded() {
        //can't use current exercise since it might be a copy of the exercise and not the same object. Therefore, getting the original object from the copy.
        getExercises().getLearnedExerciseByNumbers(getCurrentExercise().getLeftNumber(), getCurrentExercise().getRightNumber()).exerciseWasSucceeded();
    }

    public void currentExerciseNeedsRevising() {
        changeRevisingExercise(getCurrentExercise());
    }

    private void restoreLastExercise() {
        try {
            LearnedExercise exercise = LearnedExerciseManager.getSavedLastExercise(getContext(), getExercises());
            changeExercise(exercise);
        } catch (Exception e) {
            generateRandomExercise();
        }
    }

    private void restoreRevisingExercise() throws Exception {
        LearnedExercise exercise = LearnedExerciseManager.getSavedRevisingExercise(getContext(), getExercises());
        changeRevisingExercise(exercise);
    }

    private void saveLastExercise() {
        LearnedExerciseManager.saveLastExercise(getContext(), getExercises().getExerciseSign(), getCurrentExercise());
    }

    private void saveRevisingExercise() {
        LearnedExerciseManager.saveRevisingExercise(getContext(), getExercises().getExerciseSign(), getCurrentExercise());
    }

}
