package or.nevet.math_revisor.helpers;

import android.app.Activity;

import or.nevet.mathrevisorusermanager.UserManager;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.ConvertBetweenLearnedExercisesAndFireStore;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.learned_exercise_manager.LearnedExerciseManager;
import or.nevet.orgeneralhelpers.graphical.UserMessages;
import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;

public class SignInChecking {

    public static void check(String userNameText, String emailText, String passwordText, boolean isSignIn, Activity activity) throws Exception {
        checkEmpties(userNameText, emailText, passwordText, isSignIn, activity);
        userNameCheck(userNameText, isSignIn, activity);
        if (isSignIn)
            UserManager.signInNewUser(emailText, passwordText, userNameText, ConvertBetweenLearnedExercisesAndFireStore.convertExercisesMapToFirestore(LearnedExerciseManager.getMapOfLearnedExercisesUsedByTheApp()), activity);
        else
            UserManager.logInExistingUser(emailText, passwordText, activity);
    }

    private static void userNameCheck(String userNameText, boolean isSignIn, Activity activity) throws Exception {
        if (UserManager.checkIfUserNameExists(userNameText) && isSignIn) {
            UserMessages.showEmptyDialogMessage(UserMessagesConstants.userNameAlreadyExists, activity);
            throw new Exception();
        }
    }

    private static void checkEmpties(String userNameText, String emailText, String passwordText, boolean isSignIn, Activity activity) throws Exception {
        checkEmailEmpty(emailText, activity);
        if (passwordText.isEmpty()) {
            UserMessages.showEmptyDialogMessage(UserMessagesConstants.haveToEnterPassword, activity);
            throw new Exception();
        }
        if (userNameText.isEmpty() && isSignIn) {
            UserMessages.showEmptyDialogMessage(UserMessagesConstants.haveToEnterUserName, activity);
            throw new Exception();
        }
    }

    public static void checkEmailEmpty(String emailText, Activity activity) throws Exception {
        if (emailText.isEmpty()) {
            UserMessages.showEmptyDialogMessage(UserMessagesConstants.haveToEnterEmail, activity);
            throw new Exception();
        }
    }
}
