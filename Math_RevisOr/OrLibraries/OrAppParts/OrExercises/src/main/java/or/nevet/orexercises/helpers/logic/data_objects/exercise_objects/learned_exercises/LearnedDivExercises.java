package or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises;

import java.util.ArrayList;
import java.util.HashMap;

import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;

public class LearnedDivExercises extends LearnedExercises {
    public LearnedDivExercises(HashMap<String, LearnedExercise> learnedExercises, ArrayList<Integer> leftNumbersOrderedByIndexes, ArrayList<Integer> rightNumbersOrderedByIndexes) {
        super(learnedExercises, leftNumbersOrderedByIndexes, rightNumbersOrderedByIndexes, GeneralExerciseConstants.divisionSign);
    }

    public LearnedDivExercises(LearnedExercises exercises) {
        super(exercises.learnedExercises, exercises.leftNumbersOrderedByTheirIndexes, exercises.rightNumbersOrderedByTheirIndexes, GeneralExerciseConstants.divisionSign);
    }
}
