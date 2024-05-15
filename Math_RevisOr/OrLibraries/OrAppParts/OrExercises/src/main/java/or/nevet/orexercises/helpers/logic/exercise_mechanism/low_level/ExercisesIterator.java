package or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level;

import java.util.HashMap;
import java.util.Map;

import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.logic.interfaces.ExerciseIteration;
import or.nevet.orexercises.helpers.logic.interfaces.TableCellIteration;

public class ExercisesIterator {
    //Iterates only over all of the cells.
    public static void iterateAllCellsByOrder(int tableHeight, int tableWidth, TableCellIteration iteration) {
        for (int row = 0; row < tableHeight; row++)
            for (int column = 0; column < tableWidth; column++)
                iteration.onIteration(row, column);
    }

    //Iterates only over the existing exercises, as opposed to the cells iterator, which goes over all of the cells.
    public static void iterateExistingExercisesNoOrder(HashMap<String, LearnedExercise> exercises, ExerciseIteration iteration) {
        for (Map.Entry<String, LearnedExercise> exercise : exercises.entrySet())
            iteration.onIteration(exercise.getKey(), exercise.getValue());
    }

    //Iterates only over the existing exercises, as opposed to the cells iterator, which goes over all of the cells.
    public static void iterateExistingIndexesNoOrder(HashMap<String, LearnedExercise> exercises, TableCellIteration iteration) {
        for (String indexes : exercises.keySet()) {
            Map.Entry<Integer, Integer> entry = LearnedExercisesIndexesHelper.getIndexesFromStringIndexes(indexes);
            iteration.onIteration(entry.getKey(), entry.getValue());
        }
    }
}
