package or.nevet.mathrevisorusermanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import or.nevet.mathrevisorusermanager.learned_exercises_data.LearnedExerciseData;
import or.nevet.mathrevisorusermanager.learned_exercises_data.LearnedExercisesData;

public class UserObjectConverter {
    public static UserObject convert(or.nevet.orusermanager.UserObject generalUser) {
        HashMap<String, HashMap<String, ?>> generalLearnedExercisesMap = generalUser.getProperty(UserManagerConstants.userLearnedExercisesProperty);
        HashMap<String, LearnedExercisesData> learnedExercisesMap = new HashMap<>();
        for (Map.Entry<String, ?> exercisesEntry : generalLearnedExercisesMap.entrySet()) {
            try {
                LearnedExercisesData data = (LearnedExercisesData) exercisesEntry.getValue();
                learnedExercisesMap.put(exercisesEntry.getKey(), data);
            } catch (ClassCastException e) {
                HashMap<String, LearnedExerciseData> learnedExercises = new HashMap<>();
                HashMap<String, ?> variablesMap = (HashMap<String, ?>) exercisesEntry.getValue();
                Map<String, ?> generalLearnedExercises = (Map<String, ?>) variablesMap.get(UserManagerConstants.learnedExerciseDataMapVariableName);
                for (Map.Entry<String, ?> exerciseEntry : generalLearnedExercises.entrySet()) {
                    HashMap<String, ?> generalLearnedExercise = (HashMap<String, ?>) exerciseEntry.getValue();
                    learnedExercises.put(exerciseEntry.getKey(), new LearnedExerciseData(Integer.valueOf(generalLearnedExercise.get(UserManagerConstants.leftNumberVariableName).toString()), Integer.valueOf(generalLearnedExercise.get(UserManagerConstants.rightNumberVariableName).toString()), Integer.valueOf(generalLearnedExercise.get(UserManagerConstants.numOfLearnedVariableName).toString()), Integer.valueOf(generalLearnedExercise.get(UserManagerConstants.resultVariableName).toString())));
                }
                ArrayList<?> generalLeftNums = (ArrayList<Integer>) variablesMap.get(UserManagerConstants.leftNumbersArrayListVariableName);
                ArrayList<Integer> leftNums = new ArrayList<>();
                for (Object o : generalLeftNums)
                    leftNums.add(Integer.valueOf(o.toString()));
                ArrayList<?> generalRightNums = (ArrayList<Integer>) variablesMap.get(UserManagerConstants.rightNumbersArrayListVariableName);
                ArrayList<Integer> rightNums = new ArrayList<>();
                for (Object o : generalRightNums)
                    rightNums.add(Integer.valueOf(o.toString()));
                String exerciseSign = (String) variablesMap.get(UserManagerConstants.exerciseSignVariableName);
                learnedExercisesMap.put(exercisesEntry.getKey(), new LearnedExercisesData(learnedExercises, leftNums, rightNums, exerciseSign.charAt(0)));
            }
        }
        return new UserObject(new User(generalUser.getProperty(UserManagerConstants.userEmailProperty), generalUser.getProperty(UserManagerConstants.userUserNameProperty), generalUser.getProperty(UserManagerConstants.userScoreProperty), learnedExercisesMap));
    }
}
