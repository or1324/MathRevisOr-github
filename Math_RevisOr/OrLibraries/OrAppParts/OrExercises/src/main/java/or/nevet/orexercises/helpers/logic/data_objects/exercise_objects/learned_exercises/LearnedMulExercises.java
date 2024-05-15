package or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises;

import java.util.ArrayList;
import java.util.HashMap;

import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;

public class LearnedMulExercises extends LearnedExercises {
    public LearnedMulExercises(HashMap<String, LearnedExercise> learnedExercises, ArrayList<Integer> leftNumbersOrderedByIndexes, ArrayList<Integer> rightNumbersOrderedByIndexes) {
        super(learnedExercises, leftNumbersOrderedByIndexes, rightNumbersOrderedByIndexes, GeneralExerciseConstants.multiplicationSign);
    }

    public LearnedMulExercises(LearnedExercises exercises) {
        super(exercises.learnedExercises, exercises.leftNumbersOrderedByTheirIndexes, exercises.rightNumbersOrderedByTheirIndexes, GeneralExerciseConstants.multiplicationSign);
    }
}
