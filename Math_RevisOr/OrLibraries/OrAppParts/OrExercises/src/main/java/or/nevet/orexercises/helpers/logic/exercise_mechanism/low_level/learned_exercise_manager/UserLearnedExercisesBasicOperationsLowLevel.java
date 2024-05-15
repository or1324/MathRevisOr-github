package or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.learned_exercise_manager;

import android.content.Context;

import java.util.HashMap;
import java.util.Set;

import or.nevet.mathrevisorusermanager.UserManager;
import or.nevet.mathrevisorusermanager.learned_exercises_data.LearnedExercisesData;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.ConvertBetweenLearnedExercisesAndFireStore;

class UserLearnedExercisesBasicOperationsLowLevel {
    //O(1)
    public static LearnedExercises getSpecificSavedLearnedExercisesBySign(char sign, Context context) {
        HashMap<String, LearnedExercisesData> learnedExercises = UserManager.getCurrentUserObject(context).getLearnedExercises();
        return ConvertBetweenLearnedExercisesAndFireStore.convertFirestoreToExercises(UserManager.getCurrentUserObject(context).getLearnedExercises().get(String.valueOf(sign)));
    }

    //O(1)
    public static void saveSpecificLearnedExercisesToStorage(Context context, LearnedExercises exercises) {
        String exerciseSign = String.valueOf(exercises.getExerciseSign());
        UserManager.saveLearnedExercisesInRuntimeAndInStorage(exerciseSign, ConvertBetweenLearnedExercisesAndFireStore.convertExercisesToFirestore(exercises), context);
    }

    //O(1)
    public static void removeSpecificLearnedExercisesFromStorage(Context context, char sign) {
        UserManager.removeLearnedExercisesOnRuntimeAndFromStorage(String.valueOf(sign), context);
    }

    //O(n)
    public static Character[] getAllSavedSigns(Context context) {
        Set<String> signs = UserManager.getCurrentUserObject(context).getLearnedExercises().keySet();
        Character[] signsArray = new Character[signs.size()];
        int index = 0;
        for (String sign : signs)
            signsArray[index++] = sign.charAt(0);
        return signsArray;
    }

    //saves all of the exercises and their numOfLearned. O(n).
    public static void saveAllLearnedExercisesArrayToStorage(Context context, LearnedExercises[] exercisesArray) {
        HashMap<String, LearnedExercises> exercisesHashMap = new HashMap<>();
        for (LearnedExercises exercises : exercisesArray)
            exercisesHashMap.put(String.valueOf(exercises.getExerciseSign()), exercises);
        UserManager.updateAndSaveLearnedExercisesMapInStorage(ConvertBetweenLearnedExercisesAndFireStore.convertExercisesMapToFirestore(exercisesHashMap), context);
    }
}
