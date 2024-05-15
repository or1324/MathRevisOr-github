package or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.learned_exercise_manager;

import android.content.Context;

import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orgeneralhelpers.ExternalStorage;

abstract class ExerciseFileHelper {
    Context context;
    ExerciseFileHelper(Context context) {
        this.context = context;
    }

    void saveExercise(char exerciseSign, LearnedExercise exercise) {
        String fileName = getFileNameFromExerciseSign(exerciseSign);
        ExternalStorage.saveObject(context, fileName, exercise);
    }

    protected abstract String getFileNameFromExerciseSign(char exerciseSign);

    LearnedExercise restoreExercise(LearnedExercises exercises) throws Exception {
        String fileName = getFileNameFromExerciseSign(exercises.getExerciseSign());
        LearnedExercise exercise = (LearnedExercise) ExternalStorage.restoreObject(context, fileName);
        //needed in case that the exercise was edited. The result might have changed.
        return exercises.getLearnedExerciseByNumbers(exercise.getLeftNumber(), exercise.getRightNumber());
    }
}
