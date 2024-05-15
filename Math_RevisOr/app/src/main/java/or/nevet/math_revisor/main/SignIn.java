package or.nevet.math_revisor.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.widget.ProgressBar;

import or.nevet.math_revisor.R;
import or.nevet.math_revisor.helpers.AppGraphics;
import or.nevet.mathrevisorusermanager.UserManager;
import or.nevet.orgeneralhelpers.graphical.or_one_line_auto_size_edit_text.focusable.OrOneLineAutoSizeFocusableEditText;

public class SignIn extends AppCompatActivity {

    public boolean isSignIn = true;
    public OrOneLineAutoSizeFocusableEditText email;
    public OrOneLineAutoSizeFocusableEditText password;
    public OrOneLineAutoSizeFocusableEditText userName;
    public AppCompatImageButton isSignInButton;
    public AppCompatImageButton signInButton;
    public ProgressBar progressBar;
    public AppCompatTextView userNameText;
    public AppCompatTextView resetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        AppGraphics.initSignIn(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //app was closed completely.
        UserManager.removeUserFromMemory();
    }
}