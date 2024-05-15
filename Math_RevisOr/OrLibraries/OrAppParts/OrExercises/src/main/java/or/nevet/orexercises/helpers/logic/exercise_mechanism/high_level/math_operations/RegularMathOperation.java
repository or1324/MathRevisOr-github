package or.nevet.orexercises.helpers.logic.exercise_mechanism.high_level.math_operations;

import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.exercise_helpers.EnhancedExerciseHelper;
import or.nevet.orexercises.helpers.visual.ExerciseActivity;

public class RegularMathOperation extends MathOperation {
    public RegularMathOperation(ExerciseActivity activity, LearnedExercises exercises) {
        super(activity, exercises);
    }

    @Override
    protected EnhancedExerciseHelper createExerciseHelper() {
        return new EnhancedExerciseHelper(getExercises(), getExerciseActivity());
    }

    @Override
    protected void createFirstExercise() {
        getExerciseHelper().generateRandomExercise();
    }
}
