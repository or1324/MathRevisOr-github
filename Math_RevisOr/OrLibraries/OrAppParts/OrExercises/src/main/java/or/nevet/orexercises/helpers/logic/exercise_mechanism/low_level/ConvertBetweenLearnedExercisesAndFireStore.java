package or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level;

import java.util.HashMap;
import java.util.Map;

import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.mathrevisorusermanager.learned_exercises_data.LearnedExerciseData;
import or.nevet.mathrevisorusermanager.learned_exercises_data.LearnedExercisesData;

public class ConvertBetweenLearnedExercisesAndFireStore {
    public static HashMap<String, LearnedExercisesData> convertExercisesMapToFirestore(Map<String, LearnedExercises> exercisesMap) {
        HashMap<String, LearnedExercisesData> fireStoreExercisesMap = new HashMap<>();
        for (LearnedExercises exercises : exercisesMap.values()) {
            LearnedExercisesData learnedExercisesData = convertExercisesToFirestore(exercises);
            fireStoreExercisesMap.put(String.valueOf(exercises.getExerciseSign()), learnedExercisesData);
        }
        return fireStoreExercisesMap;
    }

    public static LearnedExercisesData convertExercisesToFirestore(LearnedExercises exercises) {
            HashMap<String, LearnedExerciseData> fireStoreLearnedExerciseHashMap = new HashMap<>();
            exercises.iterateExistingExercisesNoOrder((indexes, exercise) -> {
                    fireStoreLearnedExerciseHashMap.put(indexes, new LearnedExerciseData(exercise.getLeftNumber(), exercise.getRightNumber(), exercise.getNumOfLearned(), exercise.getResult()));
            });
            LearnedExercisesData learnedExercisesData = new LearnedExercisesData(fireStoreLearnedExerciseHashMap, exercises.getLeftNumbersOrderedArrayList(), exercises.getRightNumbersOrderedArrayList(), exercises.getExerciseSign());
        return learnedExercisesData;
    }

    public static LearnedExercises convertFirestoreToExercises(LearnedExercisesData learnedExercisesData) {
        HashMap<String, LearnedExercise> learnedExerciseHashMap = new HashMap<>();
        for (Map.Entry<String, LearnedExerciseData> exerciseMap : learnedExercisesData.learnedExercises.entrySet()) {
            learnedExerciseHashMap.put(exerciseMap.getKey(), new LearnedExercise(exerciseMap.getValue().leftNumber, exerciseMap.getValue().rightNumber, exerciseMap.getValue().numOfLearned, exerciseMap.getValue().result));
        }
        LearnedExercises exercises = new LearnedExercises(learnedExerciseHashMap, learnedExercisesData.leftNumbersOrderedByTheirIndexes, learnedExercisesData.rightNumbersOrderedByTheirIndexes, learnedExercisesData.exerciseSign.charAt(0));
        return exercises;
    }
}
