package or.nevet.mathrevisorusermanager;

import android.content.Context;

import java.util.HashMap;
import java.util.LinkedList;

import or.nevet.mathrevisorusermanager.learned_exercises_data.LearnedExercisesData;
import or.nevet.orgeneralhelpers.constants.CloudConstants;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;

public class UserManager {

    public static void signInNewUser(String email, String password, String userName, HashMap<String, LearnedExercisesData> defaultExercises, Context context) throws Exception {
        or.nevet.orusermanager.UserManager.signInNewUser(email, password, new HashMap<String, Object>(){{
            put(UserManagerConstants.userUserNameProperty, userName);
            put(UserManagerConstants.userEmailProperty, email);
            put(UserManagerConstants.userScoreProperty, 0L);
            put(UserManagerConstants.userLearnedExercisesProperty, defaultExercises);
        }}, context);
    }

    public static void logInExistingUser(String email, String password, Context context) throws Exception {
        or.nevet.orusermanager.UserManager.logInExistingUser(email, password, context);
    }

    public static void signOutAndRemoveUserFromStorage(Context context) {
        or.nevet.orusermanager.UserManager.signOutAndRemoveUserFromStorage(context);
    }

    public static boolean isTheCurrentUserSignedIn() {
        return or.nevet.orusermanager.UserManager.isTheCurrentUserSignedIn();
    }

    public static void restoreUser(Context context) throws Exception {
        or.nevet.orusermanager.UserManager.restoreUser(context);
    }

    //When the user is no longer needed for a long period, we need to set it to null to save memory.
    public static void removeUserFromMemory() {
        or.nevet.orusermanager.UserManager.removeUserFromMemory();
    }

    //blocks the current thread. Should be called by another thread.
    public static LinkedList<UserObject> getAllUsers() throws Exception {
        LinkedList<or.nevet.orusermanager.UserObject> generalUsers =  or.nevet.orusermanager.UserManager.getAllUsers(UserManagerConstants.userCreatedObjectPropertiesMapName+"."+UserManagerConstants.userScoreProperty);
        LinkedList<UserObject> users = new LinkedList<>();
        for (or.nevet.orusermanager.UserObject user : generalUsers)
            users.add(UserObjectConverter.convert(user));
        return users;
    }

    //blocks the current thread. Should be called by another thread.
    public static void uploadUser() throws Exception {
        or.nevet.orusermanager.UserManager.uploadUser();
    }

    public static boolean checkIfUserNameExists(String userName) throws Exception {
         return or.nevet.orusermanager.UserManager.checkIfUserNameExists(userName, UserManagerConstants.userCreatedObjectPropertiesMapName+CloudConstants.firestoreSubFieldSeparator+UserManagerConstants.userUserNameProperty);
    }

    public static void updateAndSaveScore(long newScore, Context context) {
        or.nevet.orusermanager.UserManager.updateUserPropertyAndSaveToStorage(UserManagerConstants.userScoreProperty, newScore, context);
    }

    public static void updateAndSaveLearnedExercisesMapInStorage(HashMap<String, LearnedExercisesData> learnedExercisesMap, Context context) {
        or.nevet.orusermanager.UserManager.updateUserPropertyAndSaveToStorage(UserManagerConstants.userLearnedExercisesProperty, learnedExercisesMap, context);
    }

    public static void saveLearnedExercisesInRuntimeAndInStorage(String exerciseSign, LearnedExercisesData learnedExercises, Context context) {
        HashMap<String, LearnedExercisesData> learnedExercisesMap = getCurrentUserObject(context).getLearnedExercises();
        learnedExercisesMap.put(exerciseSign, learnedExercises);
        updateAndSaveLearnedExercisesMapInStorage(learnedExercisesMap, context);
    }

    public static void removeLearnedExercisesOnRuntimeAndFromStorage(String exerciseSign, Context context) {
        HashMap<String, LearnedExercisesData> learnedExercisesMap = getCurrentUserObject(context).getLearnedExercises();
        //if this exercise was chosen to appear on lock screen, we need to change the exercise to default because this exercise is now removed.
        if (SettingsManager.getSettingsObject().getLockScreenExercisesSign() == exerciseSign.charAt(0))
            SettingsManager.saveLockScreenExerciseSign(GeneralExerciseConstants.multiplicationSign, context);
        learnedExercisesMap.remove(exerciseSign);
        updateAndSaveLearnedExercisesMapInStorage(learnedExercisesMap, context);
    }

    public static UserObject getCurrentUserObject(Context context) {
        or.nevet.orusermanager.UserObject generalUser = or.nevet.orusermanager.UserManager.getCurrentUserObject(context);
        return UserObjectConverter.convert(generalUser);
    }

    public static void sendPasswordResetEmail(String email) throws Exception {
        or.nevet.orusermanager.UserManager.sendPasswordResetEmail(email);
    }

}
