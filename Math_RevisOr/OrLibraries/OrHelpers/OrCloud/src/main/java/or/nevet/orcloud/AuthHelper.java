package or.nevet.orcloud;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthHelper {

    private static final FirebaseAuth auth = FirebaseAuth.getInstance();

    //blocks the current thread. Should be called by another thread.
    public static void signInNewUser(String email, String password) throws Exception {
        FirebaseSynchronizer<AuthResult> synchronizer = new FirebaseSynchronizer<>();
        synchronizer.waitForTaskToFinish(auth.createUserWithEmailAndPassword(email, password));
    }

    //blocks the current thread. Should be called by another thread.
    public static void logInExistingUser(String email, String password) throws Exception {
        FirebaseSynchronizer<AuthResult> synchronizer = new FirebaseSynchronizer<>();
        AuthResult result = synchronizer.waitForTaskToFinish(auth.signInWithEmailAndPassword(email, password));
    }

    public static void signOutCurrentUser() {
        auth.signOut();
    }

    public static boolean isTheCurrentUserLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    public static void sendPasswordResetEmail(String email) throws Exception {
        FirebaseSynchronizer<Void> synchronizer = new FirebaseSynchronizer<>();
        synchronizer.waitForTaskToFinish(auth.sendPasswordResetEmail(email));
    }
}
