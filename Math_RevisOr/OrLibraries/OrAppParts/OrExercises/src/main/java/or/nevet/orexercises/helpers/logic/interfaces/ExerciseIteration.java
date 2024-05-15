package or.nevet.orexercises.helpers.logic.interfaces;

import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;

public interface ExerciseIteration {
    void onIteration(String indexes, LearnedExercise exercise);
}
