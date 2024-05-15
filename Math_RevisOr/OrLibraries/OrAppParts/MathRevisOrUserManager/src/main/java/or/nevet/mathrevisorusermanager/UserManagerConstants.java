package or.nevet.mathrevisorusermanager;

import java.util.HashMap;

import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;

public class UserManagerConstants {
    public static final String settingsIsLockScreenModeEnabled = "lock_screen_enabled";
    public static final String settingsLockScreenExerciseSign = "lock_screen_sign";
    public static final HashMap<String, Object> defaultSettingsObject = new HashMap<String, Object>(){{
        put(settingsIsLockScreenModeEnabled, false);
        put(settingsLockScreenExerciseSign, GeneralExerciseConstants.multiplicationSign);
    }};

    public static final String userCreatedObjectPropertiesMapName = "properties";
    public static final String userEmailProperty = "email";
    public static final String userUserNameProperty = "userName";
    public static final String userScoreProperty = "score";
    public static final String userLearnedExercisesProperty = "learnedExercises";

    public static final String learnedExerciseDataMapVariableName = "learnedExercises";
    public static final String leftNumbersArrayListVariableName = "leftNumbersOrderedByTheirIndexes";
    public static final String rightNumbersArrayListVariableName = "rightNumbersOrderedByTheirIndexes";
    public static final String exerciseSignVariableName = "exerciseSign";
    public static final String leftNumberVariableName = "leftNumber";
    public static final String rightNumberVariableName = "rightNumber";
    public static final String resultVariableName = "result";
    public static final String numOfLearnedVariableName = "numOfLearned";
}
