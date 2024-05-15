package or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level;

import java.util.AbstractMap;
import java.util.Map;

import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;

public class LearnedExercisesIndexesHelper {
    public static String getIndexesStringFromIndexes(int leftIndex, int rightIndex) {
        return leftIndex + GeneralExerciseConstants.exercisesIndexesSplitter + rightIndex;
    }

    public static Map.Entry<Integer, Integer> getIndexesFromStringIndexes(String stringIndexes) {
        String[] indexesAsStrings = stringIndexes.split(GeneralExerciseConstants.exercisesIndexesSplitter);
        int leftIndex = Integer.parseInt(indexesAsStrings[0]);
        int rightIndex = Integer.parseInt(indexesAsStrings[1]);
        return new AbstractMap.SimpleEntry<>(leftIndex, rightIndex);
    }
}
