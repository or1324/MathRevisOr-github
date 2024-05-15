package or.nevet.orusermanager;

import android.content.Context;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import or.nevet.orcloud.AuthHelper;
import or.nevet.orcloud.FirestoreHelper;
import or.nevet.orgeneralhelpers.constants.CloudConstants;
import or.nevet.orgeneralhelpers.constants.FilesConstants;
import or.nevet.orgeneralhelpers.ExternalStorage;
import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;
import or.nevet.orgeneralhelpers.music_related.AppMusicService;

public class UserManager {
    private static User currentUser;
    public static void signInNewUser(String email, String password, HashMap<String, Object> userProperties, Context context) throws Exception {
        AuthHelper.signInNewUser(email, password);
        if (AuthHelper.isTheCurrentUserLoggedIn()) {
            signIn(new User(userProperties, email), context);
        } else
            throw new Exception();
    }

    public static void logInExistingUser(String email, String password, Context context) throws Exception {
        AuthHelper.logInExistingUser(email, password);
        if (AuthHelper.isTheCurrentUserLoggedIn()) {
            User user = getUserFromCloud(email);
            if (user == null)
                throw new RuntimeException(UserMessagesConstants.notSignedUp);
            UserManager.signIn(user, context);
        } else
            throw new Exception();
    }

    private static void signIn(User user, Context context) throws Exception {
        currentUser = user;
        saveUserToStorage(context);
        uploadUser();
    }

    public static void signOutAndRemoveUserFromStorage(Context context) {
        AuthHelper.signOutCurrentUser();
        SettingsManager.resetSettings(context);
        if (AppMusicService.getInstance() != null)
            AppMusicService.getInstance().resetMusic();
        currentUser = null;
        saveUserToStorage(context);
    }

    public static boolean isTheCurrentUserSignedIn() {
        return AuthHelper.isTheCurrentUserLoggedIn();
    }

    private static void saveUserToStorage(Context context) {
        ExternalStorage.saveObject(context, FilesConstants.userFileName, currentUser);
    }

    public static void restoreUser(Context context) throws Exception {
        currentUser = (User) ExternalStorage.restoreObject(context, FilesConstants.userFileName);
        if (currentUser == null)
            throw new Exception(UserMessagesConstants.userDoesNotExistInStorage);
    }

    //When the user is no longer needed for a long period, we need to set it to null to save memory.
    public static void removeUserFromMemory() {
        currentUser = null;
    }

    //blocks the current thread. Should be called by another thread.
    public static LinkedList<UserObject> getAllUsers(String orderBy) throws Exception {
        List<DocumentSnapshot> userDocs = FirestoreHelper.getAllDocumentsInCollectionDescendingOrder(CloudConstants.usersCollectionName, orderBy);
        LinkedList<UserObject> usersList = new LinkedList<>();
        for (DocumentSnapshot documentSnapshot : userDocs) {
            User u = documentSnapshot.toObject(User.class);
            usersList.add(new UserObject(u));
        }
        return usersList;
    }

    //blocks the current thread. Should be called by another thread.
    public static void uploadUser() throws Exception {
        if (currentUser == null)
            throw new RuntimeException(UserMessagesConstants.theUserIsNotSignedInSoImpossibleToUploadScore);
        FirestoreHelper.uploadDocumentByObject(CloudConstants.usersCollectionName+ CloudConstants.cloudLocationSeparator+currentUser.getEmail(), currentUser);
    }

    //blocks the current thread. Should be called by another thread.
    private static User getUserFromCloud(String email) throws Exception {
        DocumentSnapshot snapshot = FirestoreHelper.getDocumentFromCloud(CloudConstants.usersCollectionName+ CloudConstants.cloudLocationSeparator+email);
        return snapshot.toObject(User.class);
    }

    public static boolean checkIfUserNameExists(String userName, String userNameFieldPath) throws Exception {
         List<DocumentSnapshot> usersWithThisUserName = FirestoreHelper.getDocumentsInCollectionWithField(CloudConstants.usersCollectionName, userNameFieldPath, userName);
        if (usersWithThisUserName.isEmpty())
             return false;
         return true;
    }

    public static UserObject getCurrentUserObject(Context context) {
        if (currentUser == null) {
            try {
                restoreUser(context);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return new UserObject(currentUser);
    }

    public static void updateUserPropertyAndSaveToStorage(String propertyName, Object propertyValue, Context context) {
        currentUser.setProperty(propertyName, propertyValue);
        saveUserToStorage(context);
    }

    public static void sendPasswordResetEmail(String email) throws Exception {
        AuthHelper.sendPasswordResetEmail(email);
    }

}
