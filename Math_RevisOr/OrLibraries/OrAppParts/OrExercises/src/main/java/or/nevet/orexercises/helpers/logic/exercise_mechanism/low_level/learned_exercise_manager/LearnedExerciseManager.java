package or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.learned_exercise_manager;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedDivExercises;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedMulExercises;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.LearnedExercisesIndexesHelper;
import or.nevet.orgeneralhelpers.CommonArrayAlgorithms;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;


public class LearnedExerciseManager {

    //creates the div exercises and sets that they have not been learned at all. The left number of a div exercise in each indexes in the learnedExercise arrays is equal to its index on the first array plus one, times the index on the second array plus one. The right number of the exercise is equal to the index in the second array plus one. The result of each exercise is the division of the left number and the right number. So leftNumber/rightNumber=result. Linear complexity (if we look at the multidimensional array as one big array) and only integers. O(n*n).
    private static LearnedDivExercises generateNewLearnedDivExercises() {
        //the amount of different numbers that can be achieved from a*a when 1<a<Constants.maxNum is Constants.maxNum+(Constants.maxNum*Constants.maxNum-Constants.maxNum)/2.

        //find all multiplications of Constants.maxNum multiplication board. The number of different multiplications in a multiplication board of sizes a*a is a+(a*a-a)/2. It is the middle slant plus the multiplications without the middle slant divided by 2 (because they appear both in the top and in the bottom of the board).
        HashSet<Integer> multiplicationsSet = new HashSet<>(GeneralExerciseConstants.maxNum+(GeneralExerciseConstants.maxNum* GeneralExerciseConstants.maxNum- GeneralExerciseConstants.maxNum)/2);

        //O(n*n)
        for (int i = 1; i <= GeneralExerciseConstants.maxNum; i++)
            for (int j = 1; j <= GeneralExerciseConstants.maxNum; j++)
                multiplicationsSet.add(i*j);

        ArrayList<Integer> multiplicationsArray = new ArrayList<>(multiplicationsSet);
        Collections.sort(multiplicationsArray);
        HashMap<String, LearnedExercise> learnedDivs = new HashMap<>();
        ArrayList<Integer> leftNumbers = multiplicationsArray;
        ArrayList<Integer> rightNumbers = new ArrayList<>(GeneralExerciseConstants.maxNum);
        for (int i = 1; i <= GeneralExerciseConstants.maxNum; i++)
            rightNumbers.add(i);

        //loop through the multiplications. Each multiplication will be a number in the first row, and the numbers in the first column will be the numbers from 1 to Constants.maxNum.
        for (int row = 0; row < multiplicationsArray.size(); row++)
            for (int column = 0; column < GeneralExerciseConstants.maxNum; column++) {
                int leftNum = multiplicationsArray.get(row);
                int rightNum = column+1;
                if (leftNum % rightNum == 0) {
                    int result = leftNum / rightNum;
                    //Allow only numbers that appear in the multiplication exercises (that result*rightNum=leftNum and result<Constants.maxNum and rightNum < Constants.maxNum).
                    if (result <= GeneralExerciseConstants.maxNum)
                        learnedDivs.put(LearnedExercisesIndexesHelper.getIndexesStringFromIndexes(row, column), new LearnedExercise(leftNum, rightNum, 0, result));
                }
            }
        return new LearnedDivExercises(learnedDivs, leftNumbers, rightNumbers);
    }

    //creates the mul exercises and sets that they have not been learned at all. The left number of a mul exercise in each indexes in the learnedExercise arrays is equal to its index on the first array plus one. The right number of each exercise is equal to the index in the second array plus one. The result of each exercise is the multiplication of the left number and the right number. So leftNumber*rightNumber=result. O(n*n).
    private static LearnedMulExercises generateNewLearnedMulExercises() {
        ArrayList<Integer> leftNumbers = new ArrayList<>(GeneralExerciseConstants.maxNum);
        ArrayList<Integer> rightNumbers = new ArrayList<>(GeneralExerciseConstants.maxNum);
        for (int i = 1; i <= GeneralExerciseConstants.maxNum; i++) {
            leftNumbers.add(i);
            rightNumbers.add(i);
        }

        HashMap<String, LearnedExercise> learnedMuls = new HashMap<>(GeneralExerciseConstants.maxNum* GeneralExerciseConstants.maxNum);
        for (int indexInFirstArrayOfLearnedExercises = 0; indexInFirstArrayOfLearnedExercises < GeneralExerciseConstants.maxNum; indexInFirstArrayOfLearnedExercises++) {
            for (int indexInSecondArrayOfLearnedExercises = 0; indexInSecondArrayOfLearnedExercises < GeneralExerciseConstants.maxNum; indexInSecondArrayOfLearnedExercises++) {
                int leftNum = indexInFirstArrayOfLearnedExercises+1;
                int rightNum = indexInSecondArrayOfLearnedExercises+1;
                int result = leftNum*rightNum;
                learnedMuls.put(LearnedExercisesIndexesHelper.getIndexesStringFromIndexes(indexInFirstArrayOfLearnedExercises, indexInSecondArrayOfLearnedExercises), new LearnedExercise(leftNum, rightNum, 0, result));
            }
        }
        return new LearnedMulExercises(learnedMuls, leftNumbers, rightNumbers);
    }

    //returns the div exercises. If there are divs who were saved with their numOfLearned property, it returns them, otherwise, it returns divs with this property set to zero. Worst case: O(n*n). Best case: O(1).
    public static LearnedDivExercises getLearnedDivExercises(Context context) {
        try {
            LearnedExercises exercises = getSpecificSavedLearnedExercisesBySign(GeneralExerciseConstants.divisionSign, context);
            return new LearnedDivExercises(exercises);
        } catch (Exception e) {
            return generateNewLearnedDivExercises();
        }
    }

    //returns the mul exercises. If there are muls who were saved with their numOfLearned property, it returns them, otherwise, it returns muls with this property set to zero. Worst case: O(n*n). Best case: O(1).
    public static LearnedMulExercises getLearnedMulExercises(Context context) {
        try {
            LearnedExercises exercises = getSpecificSavedLearnedExercisesBySign(GeneralExerciseConstants.multiplicationSign, context);
            return new LearnedMulExercises(exercises);
        } catch (Exception e) {
            return generateNewLearnedMulExercises();
        }
    }

    //O(1)
    public static LearnedExercise getSavedLastExercise(Context context, LearnedExercises exercises) throws Exception {
        LastExerciseFileHelper helper = new LastExerciseFileHelper(context);
        return helper.restoreExercise(exercises);
    }

    //O(1)
    public static LearnedExercise getSavedRevisingExercise(Context context, LearnedExercises exercises) throws Exception {
        RevisingExerciseFileHelper helper = new RevisingExerciseFileHelper(context);
        return helper.restoreExercise(exercises);
    }

    //O(1)
    public static int getSavedLearningProcess(Context context, char sign) throws Exception {
        return LearningProcessFileHelper.restoreLearningProcess(context, sign);
    }

    //O(1)
    public static void saveLastExercise(Context context, char exerciseSign, LearnedExercise exercise) {
        LastExerciseFileHelper helper = new LastExerciseFileHelper(context);
        helper.saveExercise(exerciseSign, exercise);
    }

    //O(1)
    public static void saveRevisingExercise(Context context, char exerciseSign, LearnedExercise exercise) {
        RevisingExerciseFileHelper helper = new RevisingExerciseFileHelper(context);
        helper.saveExercise(exerciseSign, exercise);
    }

    //O(1)
    public static void saveLearningProcess(Context context, char sign, int learningProcess) {
        LearningProcessFileHelper.saveLearningProcess(context, sign, learningProcess);
    }

    //O(n)
    public static Character[] getAllSavedSigns(Context context) {
        return UserLearnedExercisesBasicOperationsLowLevel.getAllSavedSigns(context);
    }

    //O(1)
    public static void removeSpecificLearnedExercisesFromStorage(Context context, char sign) {
        UserLearnedExercisesBasicOperationsLowLevel.removeSpecificLearnedExercisesFromStorage(context, sign);
    }


    //O(n)
    public static Character[] getCustomExercisesSavedSigns(Context context) {
        Character[] allSigns = getAllSavedSigns(context);
        Character[] customExerciseSigns = new Character[allSigns.length-getSignsThatAreUsedByTheAppByDefault().length];
        int currentIndex = 0;
        for (char c : allSigns) {
            if (!CommonArrayAlgorithms.arrayContainsElement(getSignsThatAreUsedByTheAppByDefault(), c))
                customExerciseSigns[currentIndex++] = c;
        }
        return customExerciseSigns;
    }

    //O(1)
    public static Character[] getSignsThatAreUsedByTheAppByDefault() {
        return GeneralExerciseConstants.signsUsedByTheAppByDefault;
    }

    //saves all of the exercises and their numOfLearned. O(n).
    public static void saveAllLearnedExercisesArray(Context context, LearnedExercises[] exercisesArray) {
        UserLearnedExercisesBasicOperationsLowLevel.saveAllLearnedExercisesArrayToStorage(context, exercisesArray);
    }

    //returns all of the saved exercises that the user has created and their numOfLearned. O(n).
    public static LearnedExercises[] getAllSavedUserCreatedLearnedExercisesArray(Context context) {
        Character[] signs = getCustomExercisesSavedSigns(context);
        LearnedExercises[] learnedExercises = new LearnedExercises[signs.length];
        for (int i = 0; i < signs.length; i++)
            learnedExercises[i] = getSpecificSavedLearnedExercisesBySign(signs[i], context);
        return learnedExercises;
    }

    //O(1)
    public static LearnedExercises getSpecificSavedLearnedExercisesBySign(char sign, Context context) {
        return UserLearnedExercisesBasicOperationsLowLevel.getSpecificSavedLearnedExercisesBySign(sign, context);
    }


    //O(1)
    public static void saveSpecificLearnedExercises(Context context, LearnedExercises exercises) {
        UserLearnedExercisesBasicOperationsLowLevel.saveSpecificLearnedExercisesToStorage(context, exercises);
    }

    public static HashMap<String, LearnedExercises> getMapOfLearnedExercisesUsedByTheApp() {
        HashMap<String, LearnedExercises> usedByAppExercisesMap = new HashMap<>();
        usedByAppExercisesMap.put(String.valueOf(GeneralExerciseConstants.multiplicationSign), generateNewLearnedMulExercises());
        usedByAppExercisesMap.put(String.valueOf(GeneralExerciseConstants.divisionSign), generateNewLearnedDivExercises());
        return usedByAppExercisesMap;
    }
}
